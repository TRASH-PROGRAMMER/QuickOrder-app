package com.example.quickorderapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_messages")
data class DailyMessageEntity(
    @PrimaryKey
    val id: String,
    val titulo: String,
    val mensaje: String,
    val fechaPublicacion: Long,
    val estado: Boolean
)
