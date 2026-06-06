package com.example.quickorderapp.data.repository

import com.example.quickorderapp.data.local.dao.ProductDao
import com.example.quickorderapp.domain.model.Product
import com.example.quickorderapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementación de [ProductRepository].
 */
class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductRepository {
    
    override fun getProducts(): Flow<List<Product>> {
        return productDao.getAll().map { it.toDomainList() }
    }

    override suspend fun addProduct(product: Product) {
        productDao.insert(product.toEntity())
    }

    override suspend fun updateProduct(product: Product) {
        productDao.update(product.toEntity())
    }

    override suspend fun deleteProduct(product: Product) {
        productDao.delete(product.toEntity())
    }
}
