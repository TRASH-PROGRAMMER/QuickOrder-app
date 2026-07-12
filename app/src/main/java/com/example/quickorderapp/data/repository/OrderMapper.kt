package com.example.quickorderapp.data.repository

import com.example.quickorderapp.data.local.entities.OrderEntity
import com.example.quickorderapp.data.local.entities.OrderDetailEntity
import com.example.quickorderapp.domain.model.Order
import com.example.quickorderapp.domain.model.OrderItem

fun OrderEntity.toDomain(items: List<OrderItem> = emptyList()): Order {
    return Order(
        id = id,
        orderNumber = orderNumber,
        remoteId = remoteId,
        userEmail = userEmail,
        clienteNombre = clienteNombre,
        clienteId = clienteId,
        numeroMesa = numeroMesa,
        total = total,
        estado = estado,
        fecha = fecha,
        notas = notas,
        items = items
    )
}

fun Order.toEntity(): OrderEntity {
    return OrderEntity(
        id = id,
        orderNumber = orderNumber,
        remoteId = remoteId,
        userEmail = userEmail,
        clienteNombre = clienteNombre,
        clienteId = clienteId,
        numeroMesa = numeroMesa,
        total = total,
        estado = estado,
        fecha = fecha,
        notas = notas
    )
}

fun OrderItem.toEntity(pedidoId: Int): OrderDetailEntity {
    return OrderDetailEntity(
        id = id,
        pedidoId = pedidoId,
        productoId = productoId,
        cantidad = cantidad,
        precioUnitario = precioUnitario,
        subtotal = subtotal
    )
}

fun OrderDetailEntity.toDomain(productoNombre: String = ""): OrderItem {
    return OrderItem(
        id = id,
        productoId = productoId,
        productoNombre = productoNombre,
        cantidad = cantidad,
        precioUnitario = precioUnitario,
        subtotal = subtotal
    )
}
