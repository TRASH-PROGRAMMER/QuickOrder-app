package com.example.quickorderapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurant_info")
data class RestaurantInfoEntity(
    @PrimaryKey
    val id: String,
    val nombre: String,
    val historia: String,
    val descripcion: String,
    val direccion: String,
    val telefono: String,
    val correo: String,
    val horario: String,
    val redesSociales: String
)
