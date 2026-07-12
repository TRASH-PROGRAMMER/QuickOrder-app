package com.example.quickorderapp.domain.model

data class Category(
    val id: String = "",
    val nombre: String = "",
    val estado: Boolean = true,
    val fechaCreacion: Long = System.currentTimeMillis()
)
