package com.example.quickorderapp.data.local.entities
import androidx.room.Entity
import androidx.room.PrimaryKey
 /**
  * Representa un registro de pedido dentro de la tabla "pedidos" de la base de datos.
  */
 @Entity(tableName = "pedidos")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val numeroMesa: Int,
    val total: Double,
    val estado: String, // "Pendiente", "Preparando", "Servido", "Pagado"
    val fecha: Long = System.currentTimeMillis()
 )
