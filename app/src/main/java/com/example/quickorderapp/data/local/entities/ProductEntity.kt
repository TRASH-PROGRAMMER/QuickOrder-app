package com.example.quickorderapp.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Representa un registro de producto dentro de la base de datos local de Room.
 */
@Entity(
    tableName = "productos",
    indices = [Index(value = ["uid"], unique = true)]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) 
    val id: Int = 0,
    val uid: String,
    val nombre: String,
    val precio: Double,
    val descripcion: String,
    val imagenUrl: String = "",
    val categoria: String = "",
    val descuento: Double = 0.0,
    val esPromocion: Boolean = false,
    val ultimoCambio: Long = System.currentTimeMillis()
)
