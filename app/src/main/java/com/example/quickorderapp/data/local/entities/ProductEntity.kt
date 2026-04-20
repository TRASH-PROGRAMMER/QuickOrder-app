package com.example.quickorderapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) 
    val id: Int = 0,
    val nombre: String,
    val precio: Double,
    val descripcion: String,
    val imagenUrl: String = "",
    val categoria: String = "",
    val descuento: Double = 0.0
)
