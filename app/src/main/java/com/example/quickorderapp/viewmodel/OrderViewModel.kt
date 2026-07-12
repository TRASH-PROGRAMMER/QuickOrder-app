package com.example.quickorderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickorderapp.domain.model.Mesa
import com.example.quickorderapp.domain.model.Order
import com.example.quickorderapp.domain.model.OrderItem
import com.example.quickorderapp.domain.model.Product
import com.example.quickorderapp.domain.repository.AuthRepository
import com.example.quickorderapp.domain.repository.MesaRepository
import com.example.quickorderapp.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartState(
    val items: List<OrderItem> = emptyList(),
    val total: Double = 0.0,
    val mesaSeleccionada: Int? = null,
    val notas: String = ""
)

data class OrderHistoryState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface OrderActionStatus {
    data object Idle : OrderActionStatus
    data object Loading : OrderActionStatus
    data class Success(val order: Order) : OrderActionStatus
    data class Error(val message: String) : OrderActionStatus
}

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val mesaRepository: MesaRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _cartState = MutableStateFlow(CartState())
    val cartState: StateFlow<CartState> = _cartState.asStateFlow()

    private val _orderActionStatus = MutableStateFlow<OrderActionStatus>(OrderActionStatus.Idle)
    val orderActionStatus: StateFlow<OrderActionStatus> = _orderActionStatus.asStateFlow()

    val mesas: StateFlow<List<Mesa>> = mesaRepository.getMesas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userEmail: StateFlow<String> = authRepository.getUserEmail()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val orderHistory: StateFlow<OrderHistoryState> = userEmail
        .flatMapLatest { email ->
            if (email.isEmpty()) flowOf(OrderHistoryState())
            else orderRepository.getOrdersByUser(email).map { OrderHistoryState(orders = it) }
        }
        .onStart { /* loading state if needed */ }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OrderHistoryState(isLoading = true))

    fun addToCart(product: Product) {
        _cartState.update { current ->
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

    fun removeFromCart(productId: Int) {
        _cartState.update { current ->
            val newItems = current.items.filterNot { it.productoId == productId }
            current.copy(items = newItems, total = newItems.sumOf { it.subtotal })
        }
    }

    fun updateQuantity(productId: Int, delta: Int) {
        _cartState.update { current ->
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

    fun setProductQuantity(product: Product, quantity: Int) {
        _cartState.update { current ->
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

    fun setMesa(numero: Int) {
        _cartState.update { it.copy(mesaSeleccionada = numero) }
    }

    fun setNotas(notas: String) {
        _cartState.update { it.copy(notas = notas) }
    }

    fun confirmOrder() {
        val state = _cartState.value
        
        if (state.items.isEmpty()) {
            _orderActionStatus.value = OrderActionStatus.Error("No hay productos seleccionados")
            return
        }
        if (state.mesaSeleccionada == null) {
            _orderActionStatus.value = OrderActionStatus.Error("Debes seleccionar una mesa")
            return
        }

        viewModelScope.launch {
            _orderActionStatus.value = OrderActionStatus.Loading
            try {
                // Obtenemos email y nombre directamente del Flow para asegurar datos reales
                val email = authRepository.getUserEmail().firstOrNull() ?: ""
                val nombre = authRepository.getUserName().firstOrNull() ?: "Cliente"
                
                if (email.isEmpty()) {
                    _orderActionStatus.value = OrderActionStatus.Error("Sesión no válida. Por favor, reinicia sesión.")
                    return@launch
                }

                val orderToSave = Order(
                    userEmail = email,
                    clienteNombre = nombre,
                    numeroMesa = state.mesaSeleccionada,
                    total = state.total,
                    notas = state.notas,
                    items = state.items,
                    estado = "PENDIENTE",
                    fecha = System.currentTimeMillis()
                )
                
                val success = orderRepository.saveOrder(orderToSave)
                
                if (success) {
                    _cartState.update { CartState() } // Limpiar pedido tras éxito
                    _orderActionStatus.value = OrderActionStatus.Success(orderToSave)
                } else {
                    _orderActionStatus.value = OrderActionStatus.Error("Error al guardar el pedido")
                }
            } catch (e: Exception) {
                _orderActionStatus.value = OrderActionStatus.Error("Error crítico: ${e.localizedMessage}")
            }
        }
    }

    fun cancelOrder(orderId: Int, remoteId: String) {
        viewModelScope.launch {
            _orderActionStatus.value = OrderActionStatus.Loading
            val success = orderRepository.updateOrderStatus(orderId, remoteId, "CANCELADO")
            if (success) {
                _orderActionStatus.value = OrderActionStatus.Idle // Or a specific cancel success state
            } else {
                _orderActionStatus.value = OrderActionStatus.Error("No se pudo cancelar el pedido")
            }
        }
    }

    fun editOrder(order: Order) {
        // Cargar los items del pedido de nuevo al carrito para editar
        _cartState.update { current ->
            CartState(
                items = order.items,
                total = order.total,
                mesaSeleccionada = order.numeroMesa,
                notas = order.notas
            )
        }
    }

    fun resetActionStatus() {
        _orderActionStatus.value = OrderActionStatus.Idle
    }

    fun getOrderById(id: Int): Flow<Order?> = orderRepository.getOrderById(id)
}
