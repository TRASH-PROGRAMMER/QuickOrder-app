package com.example.quickorderapp.domain.model

/**
 * Modelo de dominio para un Usuario.
 */
data class User(
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val rol: String, // "COMENSAL", "MESERO", "ADMIN"
    val password: String
)
