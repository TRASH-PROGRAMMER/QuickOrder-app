package com.example.quickorderapp.domain.repository

import com.example.quickorderapp.domain.model.Category
import com.example.quickorderapp.domain.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz de repositorio que define las operaciones de datos para productos.
 */
interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    fun getCategories(): Flow<List<Category>>
    suspend fun addProduct(product: Product): Boolean
    suspend fun updateProduct(product: Product): Boolean
    suspend fun deleteProduct(product: Product): Boolean
}
