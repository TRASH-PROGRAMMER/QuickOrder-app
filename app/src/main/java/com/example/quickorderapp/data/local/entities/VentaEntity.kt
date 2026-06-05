package com.example.quickorderapp.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad que representa la finalización de un pedido (Venta).
 */
@Entity(
    tableName = "ventas",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["pedidoId"],
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    indices = [Index(value = ["pedidoId"])]
)
data class VentaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pedidoId: Int,
    val fecha: Long = System.currentTimeMillis(),
    val total: Double,
    val metodoPago: String // "Efectivo", "Tarjeta", "Transferencia"
)
