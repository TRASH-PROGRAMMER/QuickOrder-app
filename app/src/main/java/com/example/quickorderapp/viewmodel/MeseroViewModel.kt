package com.example.quickorderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickorderapp.domain.model.*
import com.example.quickorderapp.domain.repository.AuthRepository
import com.example.quickorderapp.domain.repository.MesaRepository
import com.example.quickorderapp.domain.repository.OrderRepository
import com.example.quickorderapp.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MeseroOrdersState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val filter: String = "TODOS" // TODOS, PENDIENTE, EN PREPARACIÓN, COMPLETADO, CANCELADO
)

@HiltViewModel
class MeseroViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val mesaRepository: MesaRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _filter = MutableStateFlow("TODOS")
    val filter: StateFlow<String> = _filter.asStateFlow()

    val allOrders: StateFlow<MeseroOrdersState> = combine(
        orderRepository.getAllOrders(),
        _filter
    ) { orders, filter ->
        val filtered = if (filter == "TODOS") orders else orders.filter { it.estado == filter }
        MeseroOrdersState(orders = filtered, isLoading = false)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MeseroOrdersState(isLoading = true))

    fun setFilter(newFilter: String) {
        _filter.value = newFilter
    }

    fun updateOrderStatus(order: Order, newStatus: String) {
        viewModelScope.launch {
            orderRepository.updateOrderStatus(order.id, order.remoteId, newStatus)
        }
    }

    // Reuse Cart logic for manual registration
    private val _manualCart = MutableStateFlow(CartState())
    val manualCart: StateFlow<CartState> = _manualCart.asStateFlow()

    fun addToManualCart(product: Product) {
        _manualCart.update { current ->
            val existing = current.items.find { it.productoId == product.id }
            val newItems = if (existing != null) {
                current.items.map {
                    if (it.productoId == product.id) it.copy(
                        cantidad = it.cantidad + 1,
                        subtotal = (it.cantidad + 1) * it.precioUnitario
                    ) else it
                }
            } else {
                current.items + OrderItem(
                    productoId = product.id,
                    productoNombre = product.nombre,
                    cantidad = 1,
                    precioUnitario = product.precio,
                    subtotal = product.precio
                )
            }
            current.copy(items = newItems, total = newItems.sumOf { it.subtotal })
        }
    }

    fun updateManualQuantity(productId: Int, delta: Int) {
        _manualCart.update { current ->
            val newItems = current.items.mapNotNull {
                if (it.productoId == productId) {
                    val newQty = it.cantidad + delta
                    if (newQty > 0) it.copy(cantidad = newQty, subtotal = newQty * it.precioUnitario)
                    else null
                } else it
            }
            current.copy(items = newItems, total = newItems.sumOf { it.subtotal })
        }
    }

    fun setManualQuantity(product: Product, quantity: Int) {
        _manualCart.update { current ->
            val existing = current.items.find { it.productoId == product.id }
            val unitPrice = if (product.descuento > 0) {
                product.precio * (1 - (product.descuento / 100))
            } else {
                product.precio
            }
            
            val newItems = if (quantity <= 0) {
                current.items.filterNot { it.productoId == product.id }
            } else if (existing != null) {
                current.items.map {
                    if (it.productoId == product.id) it.copy(
                        cantidad = quantity,
                        precioUnitario = unitPrice,
                        subtotal = quantity * unitPrice
                    ) else it
                }
            } else {
                current.items + OrderItem(
                    productoId = product.id,
                    productoNombre = product.nombre,
                    cantidad = quantity,
                    precioUnitario = unitPrice,
                    subtotal = quantity * unitPrice
                )
            }
            current.copy(items = newItems, total = newItems.sumOf { it.subtotal })
        }
    }

    private val _orderActionStatus = MutableStateFlow<OrderActionStatus>(OrderActionStatus.Idle)
    val orderActionStatus: StateFlow<OrderActionStatus> = _orderActionStatus.asStateFlow()

    fun confirmManualOrder(clienteNombre: String, clienteId: String, mesa: Int, notas: String) {
        viewModelScope.launch {
            _orderActionStatus.value = OrderActionStatus.Loading
            try {
                val order = Order(
                    clienteNombre = clienteNombre,
                    clienteId = clienteId,
                    numeroMesa = mesa,
                    total = _manualCart.value.total,
                    notas = notas,
                    items = _manualCart.value.items,
                    userEmail = "MESERO", // Indicar que fue registro manual
                    estado = "PENDIENTE",
                    fecha = System.currentTimeMillis()
                )
                val success = orderRepository.saveOrder(order)
                if (success) {
                    _manualCart.value = CartState()
                    _orderActionStatus.value = OrderActionStatus.Success(order)
                } else {
                    _orderActionStatus.value = OrderActionStatus.Error("Error al registrar pedido manual")
                }
            } catch (e: Exception) {
                _orderActionStatus.value = OrderActionStatus.Error("Fallo: ${e.message}")
            }
        }
    }

    fun resetActionStatus() {
        _orderActionStatus.value = OrderActionStatus.Idle
    }
}
