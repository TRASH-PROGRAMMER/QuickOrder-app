package com.example.quickorderapp.data.repository

import com.example.quickorderapp.data.local.dao.ProductDao
import com.example.quickorderapp.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductRepository {
    
    override fun getProducts(): Flow<List<ProductEntity>> {
            return productDao.getAll()
    }

    override suspend fun addProduct(product: ProductEntity) {
        // Corregido: El método en tu ProductDao es 'insertAll'
        productDao.insertAll(product)
    }
}
