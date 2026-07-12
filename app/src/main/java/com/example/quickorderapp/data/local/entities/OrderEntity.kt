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
    val orderNumber: Int = 0,
    val remoteId: String = "",
    val userEmail: String = "",
    val clienteNombre: String = "",
    val clienteId: String = "",
    val numeroMesa: Int,
    val total: Double,
    val estado: String, // "PENDIENTE", "EN PREPARACIÓN", "COMPLETADO", "CANCELADO"
    val fecha: Long = System.currentTimeMillis(),
    val notas: String = ""
)
