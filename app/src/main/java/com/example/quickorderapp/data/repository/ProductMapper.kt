package com.example.quickorderapp.data.repository

import com.example.quickorderapp.data.local.entities.ProductEntity
import com.example.quickorderapp.domain.model.Product

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        imagenUrl = imagenUrl,
        categoria = categoria,
        descuento = descuento,
        esPromocion = esPromocion,
        ultimoCambio = ultimoCambio
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        imagenUrl = imagenUrl,
        categoria = categoria,
        descuento = descuento,
        esPromocion = esPromocion,
        ultimoCambio = ultimoCambio
    )
}

fun List<ProductEntity>.toDomainList(): List<Product> = map { it.toDomain() }
