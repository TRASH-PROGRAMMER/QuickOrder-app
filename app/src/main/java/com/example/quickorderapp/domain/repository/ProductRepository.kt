package com.example.quickorderapp.domain.repository

import com.example.quickorderapp.domain.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz de repositorio que define las operaciones de datos para productos.
 * Utiliza modelos de dominio para mantener la independencia de la capa de datos.
 */
interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    suspend fun addProduct(product: Product)
}
