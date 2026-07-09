package com.example.quickorderapp.domain.model

/**
 * Modelo de dominio para una Mesa.
 */
data class Mesa(
    val numero: Int,
    val capacidad: Int,
    val estado: String = "Libre", // "Libre", "Ocupada", "Reservada"
    val qrCode: String? = null
)
