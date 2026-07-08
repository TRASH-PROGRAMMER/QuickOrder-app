package com.example.quickorderapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa a un usuario del sistema (Mesero, Administrador).
 */
@Entity(tableName = "usuarios")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val rol: String, // "COMENSAL", "MESERO", "ADMIN"
    val password: String // En una app real, esto sería un token o hash
)
