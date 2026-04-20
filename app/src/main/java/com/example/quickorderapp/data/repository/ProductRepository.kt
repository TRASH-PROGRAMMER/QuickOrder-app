package com.example.quickorderapp.data.repository
import com.example.quickorderapp.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow


interface ProductRepository {
     fun getProducts(): Flow<List<ProductEntity>>
     suspend fun addProduct(product: ProductEntity)
}

