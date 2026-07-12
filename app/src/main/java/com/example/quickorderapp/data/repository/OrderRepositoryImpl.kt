package com.example.quickorderapp.data.repository

import com.example.quickorderapp.data.local.dao.OrderDao
import com.example.quickorderapp.data.local.dao.OrderDetailDao
import com.example.quickorderapp.data.local.dao.ProductDao
import com.example.quickorderapp.data.remote.firebase.FirebaseOrderDataSource
import com.example.quickorderapp.data.remote.firebase.FirebaseSyncManager
import com.example.quickorderapp.domain.model.Order
import com.example.quickorderapp.domain.model.OrderItem
import com.example.quickorderapp.domain.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val orderDetailDao: OrderDetailDao,
    private val productDao: ProductDao,
    private val firebaseOrderDataSource: FirebaseOrderDataSource,
    private val syncManager: FirebaseSyncManager
) : OrderRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllOrders(): Flow<List<Order>> {
        return orderDao.getAll().flatMapLatest { entities ->
            val orderFlows = entities.map { entity ->
                orderDetailDao.getDetailsByOrderId(entity.id).flatMapLatest { details ->
                    val itemFlows = details.map { detail ->
                        productDao.getByIdFlow(detail.productoId).map { product ->
                            detail.toDomain(product?.nombre ?: "Producto desconocido")
                        }
                    }
                    if (itemFlows.isEmpty()) flowOf(entity.toDomain(emptyList()))
                    else combine(itemFlows) { itemsArray ->
                        itemsArray.toList()
                    }.map { items -> entity.toDomain(items) }
                }
            }
            if (orderFlows.isEmpty()) flowOf(emptyList())
            else combine(orderFlows) { it.toList() }
        }
    }

    override suspend fun saveOrder(order: Order): Boolean = withContext(Dispatchers.IO) {
        try {
            val maxNumber = orderDao.getMaxOrderNumber() ?: 0
            val newNumber = maxNumber + 1
            
            // 1. Guardado Local Obligatorio (SSOT)
            val entity = order.copy(orderNumber = newNumber).toEntity()
            val orderId = orderDao.insert(entity).toInt()
            
            if (orderId <= 0) return@withContext false

            val details = order.items.map { it.toEntity(orderId) }
            orderDetailDao.insertAll(details)

            // 2. Sincronización con Firebase (No bloqueante para la UI)
            if (syncManager.hasInternetConnection()) {
                try {
                    val remoteId = firebaseOrderDataSource.saveOrder(order.copy(id = orderId, orderNumber = newNumber))
                    if (remoteId.isNotEmpty()) {
                        orderDao.update(entity.copy(id = orderId, remoteId = remoteId))
                    }
                } catch (e: Exception) {
                    // Si falla Firebase, el pedido ya está en Room, se sincronizará luego
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getOrdersByUser(email: String): Flow<List<Order>> {
        return orderDao.getByUserEmail(email).flatMapLatest { entities ->
            val orderFlows = entities.map { entity ->
                orderDetailDao.getDetailsByOrderId(entity.id).flatMapLatest { details ->
                    val itemFlows = details.map { detail ->
                        productDao.getByIdFlow(detail.productoId).map { product ->
                            detail.toDomain(product?.nombre ?: "Producto desconocido")
                        }
                    }
                    if (itemFlows.isEmpty()) flowOf(entity.toDomain(emptyList()))
                    else combine(itemFlows) { itemsArray ->
                        itemsArray.toList()
                    }.map { items -> entity.toDomain(items) }
                }
            }
            if (orderFlows.isEmpty()) flowOf(emptyList())
            else combine(orderFlows) { it.toList() }
        }
    }

    override suspend fun updateOrderStatus(orderId: Int, remoteId: String, newStatus: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val entity = orderDao.getById(orderId)
            if (entity != null) {
                orderDao.update(entity.copy(estado = newStatus))
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
        return flow {
            val entity = orderDao.getById(id)
            if (entity != null) {
                orderDetailDao.getDetailsByOrderId(id).flatMapLatest { details ->
                    val itemFlows = details.map { detail ->
                        productDao.getByIdFlow(detail.productoId).map { product ->
                            detail.toDomain(product?.nombre ?: "Producto desconocido")
                        }
                    }
                    if (itemFlows.isEmpty()) flowOf(entity.toDomain(emptyList()))
                    else combine(itemFlows) { itemsArray ->
                        itemsArray.toList()
                    }.map { items -> entity.toDomain(items) }
                }.collect { fullOrder ->
                    emit(fullOrder)
                }
            } else {
                emit(null)
            }
        }
    }
}
