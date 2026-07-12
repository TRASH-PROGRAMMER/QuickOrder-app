package com.example.quickorderapp.domain.model

data class DailyMessage(
    val id: String = "",
    val titulo: String = "",
    val mensaje: String = "",
    val fechaPublicacion: Long = System.currentTimeMillis(),
    val estado: Boolean = true
)
