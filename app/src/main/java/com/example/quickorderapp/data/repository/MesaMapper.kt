package com.example.quickorderapp.data.repository

import com.example.quickorderapp.data.local.entities.MesaEntity
import com.example.quickorderapp.domain.model.Mesa

fun MesaEntity.toDomain(): Mesa {
    return Mesa(
        numero = numero,
        capacidad = capacidad,
        estado = estado,
        qrCode = qrCode
    )
}

fun Mesa.toEntity(): MesaEntity {
    return MesaEntity(
        numero = numero,
        capacidad = capacidad,
        estado = estado,
        qrCode = qrCode
    )
}

fun List<MesaEntity>.toDomainList(): List<Mesa> = map { it.toDomain() }
