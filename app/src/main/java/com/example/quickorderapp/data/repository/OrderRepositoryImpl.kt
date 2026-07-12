package com.example.quickorderapp.data.repository

import com.example.quickorderapp.data.local.dao.MesaDao
import com.example.quickorderapp.data.local.dao.OrderDao
import com.example.quickorderapp.data.local.dao.*
import com.example.quickorderapp.data.remote.firebase.FirebaseMesaDataSource
import com.example.quickorderapp.data.remote.firebase.FirebaseOrderDataSource
import com.example.quickorderapp.data.remote.firebase.FirebaseSyncManager
import com.example.quickorderapp.domain.model.Order
import com.example.quickorderapp.domain.model.OrderItem
import com.example.quickorderapp.domain.repository.OrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val orderDetailDao: OrderDetailDao,
    private val productDao: ProductDao,
    private val mesaDao: MesaDao,
    private val userDao: UserDao,
    private val firebaseOrderDataSource: FirebaseOrderDataSource,
    private val firebaseMesaDataSource: FirebaseMesaDataSource,
    private val syncManager: FirebaseSyncManager
) : OrderRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllOrders(): Flow<List<Order>> {
        return orderDao.getAll().flatMapLatest { entities ->
            if (entities.isEmpty()) return@flatMapLatest flowOf(emptyList())
            
            val orderFlows = entities.map { entity ->
                orderDetailDao.getDetailsByOrderId(entity.id).flatMapLatest { details ->
                    // Resolve items names and client name if missing
                    val itemFlows = details.map { detail ->
                        productDao.getByIdFlow(detail.productoId).map { product ->
                            detail.toDomain(product?.nombre ?: "Producto desconocido")
                        }
                    }
                    combine(itemFlows) { itemsArray ->
                        val items = itemsArray.toList()
                        
                        // Si falta el nombre del cliente, intentamos buscarlo localmente
                        val name = if (entity.clienteNombre.isBlank() && entity.userEmail.isNotBlank()) {
                            userDao.getUserByEmail(entity.userEmail)?.nombre ?: "Comensal App"
                        } else {
                            entity.clienteNombre
                        }
                        
                        entity.copy(clienteNombre = name).toDomain(items)
                    }
                }
            }
            combine(orderFlows) { it.toList() }
        }
    }

    override suspend fun saveOrder(order: Order): Boolean = withContext(Dispatchers.IO) {
        try {
            val maxNumber = orderDao.getMaxOrderNumber() ?: 0
            val newNumber = maxNumber + 1
            
            // 1. Guardado Local Obligatorio (Fuente de Verdad)
            val entity = order.copy(orderNumber = newNumber).toEntity()
            val orderId = orderDao.insert(entity).toInt()
            
            if (orderId <= 0) return@withContext false

            val details = order.items.map { it.toEntity(orderId) }
            orderDetailDao.insertAll(details)

            // 2. Actualizar estado de la Mesa a OCUPADA localmente
            try {
                val mesaEntity = mesaDao.getByNumero(order.numeroMesa)
                if (mesaEntity != null) {
                    val updatedMesa = mesaEntity.copy(estado = "Ocupada")
                    mesaDao.update(updatedMesa)
                    // Sincronización de mesa en segundo plano
                    if (syncManager.hasInternetConnection()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            firebaseMesaDataSource.addMesa(updatedMesa.toDomain())
                        }
                    }
                }
            } catch (e: Exception) { }

            // 3. Sincronización con Firebase (NO BLOQUEANTE para la UI)
            // Esto evita el "Loading Infinito" si Firebase tarda en responder
            if (syncManager.hasInternetConnection()) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val remoteId = firebaseOrderDataSource.saveOrder(order.copy(id = orderId, orderNumber = newNumber))
                        if (remoteId.isNotEmpty()) {
                            orderDao.update(entity.copy(id = orderId, remoteId = remoteId))
                        }
                    } catch (e: Exception) { }
                }
            }
            
            true // Retornamos éxito inmediato tras asegurar Room
        } catch (e: Exception) {
            false
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getOrdersByUser(email: String): Flow<List<Order>> {
        return orderDao.getByUserEmail(email).flatMapLatest { entities ->
            if (entities.isEmpty()) return@flatMapLatest flowOf(emptyList())
            
            val orderFlows = entities.map { entity ->
                orderDetailDao.getDetailsByOrderId(entity.id).flatMapLatest { details ->
                    val itemFlows = details.map { detail ->
                        productDao.getByIdFlow(detail.productoId).map { product ->
                            detail.toDomain(product?.nombre ?: "Producto desconocido")
                        }
                    }
                    combine(itemFlows) { itemsArray ->
                        val items = itemsArray.toList()
                        
                        val name = if (entity.clienteNombre.isBlank() && entity.userEmail.isNotBlank()) {
                            userDao.getUserByEmail(entity.userEmail)?.nombre ?: "Comensal App"
                        } else {
                            entity.clienteNombre
                        }
                        
                        entity.copy(clienteNombre = name).toDomain(items)
                    }
                }
            }
            combine(orderFlows) { it.toList() }
        }
    }

    override suspend fun updateOrderStatus(orderId: Int, remoteId: String, newStatus: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val entity = orderDao.getById(orderId)
            if (entity != null) {
                val updatedOrder = entity.copy(estado = newStatus)
                orderDao.update(updatedOrder)
                
                // Si el pedido se completa o cancela, liberar la mesa
                if (newStatus == "COMPLETADO" || newStatus == "CANCELADO") {
                    try {
                        val mesaEntity = mesaDao.getByNumero(entity.numeroMesa)
                        if (mesaEntity != null) {
                            val updatedMesa = mesaEntity.copy(estado = "Libre")
                            mesaDao.update(updatedMesa)
                            if (syncManager.hasInternetConnection()) {
                                firebaseMesaDataSource.addMesa(updatedMesa.toDomain())
                            }
                        }
                    } catch (e: Exception) { }
                }
            }

            if (syncManager.hasInternetConnection() && remoteId.isNotEmpty()) {
                firebaseOrderDataSource.updateOrderStatus(remoteId, newStatus)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun syncAllOrders() = withContext(Dispatchers.IO) {
        // Implementación futura si es necesaria para sincronización masiva
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getOrderById(id: Int): Flow<Order?> {
        return orderDao.getByIdFlow(id).flatMapLatest { entity ->
            if (entity == null) return@flatMapLatest flowOf(null)
            
            orderDetailDao.getDetailsByOrderId(id).flatMapLatest { details ->
                if (details.isEmpty()) return@flatMapLatest flowOf(entity.toDomain(emptyList()))
                
                val itemFlows = details.map { detail ->
                    productDao.getByIdFlow(detail.productoId).map { product ->
                        detail.toDomain(product?.nombre ?: "Producto desconocido")
                    }
                }
                combine(itemFlows) { itemsArray ->
                    entity.toDomain(itemsArray.toList())
                }
            }
        }
    }
}
