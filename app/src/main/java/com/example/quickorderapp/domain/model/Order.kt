package com.example.quickorderapp.domain.model

data class Order(
    val id: Int = 0,
    val orderNumber: Int = 0,
    val remoteId: String = "",
    val userEmail: String = "",
    val clienteNombre: String = "",
    val clienteId: String = "",
    val numeroMesa: Int = 0,
    val total: Double = 0.0,
    val estado: String = "PENDIENTE", // PENDIENTE, EN PREPARACIÓN, COMPLETADO, CANCELADO
    val fecha: Long = System.currentTimeMillis(),
    val notas: String = "",
    val items: List<OrderItem> = emptyList()
)

data class OrderItem(
    val id: Int = 0,
    val productoId: Int = 0,
    val productoNombre: String = "",
    val cantidad: Int = 0,
    val precioUnitario: Double = 0.0,
    val subtotal: Double = 0.0
)
