package com.example.quickorderapp.domain.model

data class RestaurantInfo(
    val id: String = "rest_info",
    val nombre: String = "",
    val historia: String = "",
    val descripcion: String = "",
    val direccion: String = "",
    val telefono: String = "",
    val correo: String = "",
    val horario: String = "",
    val redesSociales: String = ""
)
