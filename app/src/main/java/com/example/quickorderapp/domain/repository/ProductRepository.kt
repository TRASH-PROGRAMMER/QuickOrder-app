package com.example.quickorderapp.domain.repository

import com.example.quickorderapp.domain.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz de repositorio que define las operaciones de datos para productos.
 */
interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    suspend fun addProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)
}
