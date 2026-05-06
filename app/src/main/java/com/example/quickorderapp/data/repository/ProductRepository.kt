package com.example.quickorderapp.data.repository
import com.example.quickorderapp.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow


/**
 * Interfaz de repositorio que define las operaciones de datos para [ProductEntity].
 */
interface ProductRepository {
     fun getProducts(): Flow<List<ProductEntity>>
     suspend fun addProduct(product: ProductEntity)
}

