package com.example.quickorderapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa una mesa del restaurante.
 */
@Entity(tableName = "mesas")
data class MesaEntity(
    @PrimaryKey
    val numero: Int,
    val capacidad: Int,
    val estado: String = "Libre", // "Libre", "Ocupada", "Reservada"
    val qrCode: String? = null
)
