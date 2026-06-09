package com.example.quickorderapp.data.repository

import com.example.quickorderapp.data.local.entities.UserEntity
import com.example.quickorderapp.domain.model.User

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        nombre = nombre,
        correo = correo,
        rol = rol,
        password = password
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        nombre = nombre,
        correo = correo,
        rol = rol,
        password = password
    )
}
