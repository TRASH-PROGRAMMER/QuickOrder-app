package com.example.quickorderapp.data.local.entities
import androidx.room.Entity
import androidx.room.PrimaryKey
 /**
  * Representa un registro de pedido dentro de la tabla "pedidos" de la base de datos.
  */
 @Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tableNumber: Int,
    val total: Double,
    val status: String
 )
