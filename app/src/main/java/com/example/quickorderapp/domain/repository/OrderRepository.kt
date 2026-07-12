package com.example.quickorderapp.domain.repository

import com.example.quickorderapp.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getOrdersByUser(email: String): Flow<List<Order>>
    fun getAllOrders(): Flow<List<Order>>
    suspend fun saveOrder(order: Order): Boolean
    suspend fun updateOrderStatus(orderId: Int, remoteId: String, newStatus: String): Boolean
    fun getOrderById(id: Int): Flow<Order?>
    suspend fun syncAllOrders()
}
