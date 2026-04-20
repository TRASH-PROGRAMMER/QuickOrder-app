package com.example.quickorderapp.data.local.entities
import androidx.room.Entity
import androidx.room.PrimaryKey
 @Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tableNumber: Int,
    val total: Double,
    val status: String
 )
