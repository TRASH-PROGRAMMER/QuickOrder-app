package com.example.quickorderapp.domain.model

/**
 * Modelo de dominio para un Producto.
 * Independiente de la implementación de persistencia (Room).
 */
data class Product(
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagenUrl: String = "",
    val categoria: String = "",
    val descuento: Double = 0.0
)
