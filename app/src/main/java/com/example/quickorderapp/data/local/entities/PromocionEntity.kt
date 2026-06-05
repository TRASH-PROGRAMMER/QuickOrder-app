package com.example.quickorderapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa una promoción o descuento.
 */
@Entity(tableName = "promociones")
data class PromocionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val porcentajeDescuento: Double,
    val fechaInicio: Long,
    val fechaFin: Long,
    val activo: Boolean = true
)
