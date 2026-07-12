package com.example.quickorderapp.data.repository

import com.example.quickorderapp.data.local.entities.CategoryEntity
import com.example.quickorderapp.data.local.entities.DailyMessageEntity
import com.example.quickorderapp.data.local.entities.RestaurantInfoEntity
import com.example.quickorderapp.domain.model.Category
import com.example.quickorderapp.domain.model.DailyMessage
import com.example.quickorderapp.domain.model.RestaurantInfo

fun CategoryEntity.toDomain(): Category = Category(id, nombre, estado, fechaCreacion)
fun Category.toEntity(): CategoryEntity = CategoryEntity(id, nombre, estado, fechaCreacion)
fun List<CategoryEntity>.toCategoryDomainList(): List<Category> = map { it.toDomain() }

fun DailyMessageEntity.toDomain(): DailyMessage = DailyMessage(id, titulo, mensaje, fechaPublicacion, estado)
fun DailyMessage.toEntity(): DailyMessageEntity = DailyMessageEntity(id, titulo, mensaje, fechaPublicacion, estado)
fun List<DailyMessageEntity>.toDailyMessageDomainList(): List<DailyMessage> = map { it.toDomain() }

fun RestaurantInfoEntity.toDomain(): RestaurantInfo = RestaurantInfo(id, nombre, historia, descripcion, direccion, telefono, correo, horario, redesSociales)
fun RestaurantInfo.toEntity(): RestaurantInfoEntity = RestaurantInfoEntity(id, nombre, historia, descripcion, direccion, telefono, correo, horario, redesSociales)
