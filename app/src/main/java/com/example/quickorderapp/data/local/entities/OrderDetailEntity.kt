package com.example.quickorderapp.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Representa una línea de artículo específica dentro de un pedido en la base de datos local.
 *
 * Esta entidad mantiene una relación de muchos a uno con [OrderEntity].
 * Si se elimina el pedido asociado, todos los detalles del pedido relacionado se eliminan automáticamente.
 */
@Entity(
    tableName = "order_details",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["orderId"])]
)
data class OrderDetailEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: Int,
    val productId: Int,
    val quantity: Int,
    val subtotal: Double
)
