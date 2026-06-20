package com.example.quickorderapp.data.repository

import com.example.quickorderapp.data.local.dao.ProductDao
import com.example.quickorderapp.data.remote.firebase.FirebaseProductDataSource
import com.example.quickorderapp.data.remote.firebase.FirebaseSyncManager
import com.example.quickorderapp.domain.model.Product
import com.example.quickorderapp.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val firebaseProductDataSource: FirebaseProductDataSource,
    private val syncManager: FirebaseSyncManager
) : ProductRepository {
    
    override fun getProducts(): Flow<List<Product>> {
        // Disparar sincronización cuando se piden los productos si hay red
        if (syncManager.hasInternetConnection()) {
            syncManager.syncAll()
        }
        return productDao.getAll().map { it.toDomainList() }
    }

    override suspend fun addProduct(product: Product) = withContext(Dispatchers.IO) {
        // Guardar local primero (MAD Skill: delegación explícita)
        productDao.insert(product.toEntity())
        
        // Sincronizar con Firebase
        if (syncManager.hasInternetConnection()) {
            firebaseProductDataSource.addProduct(product)
        }
    }

    override suspend fun updateProduct(product: Product) = withContext(Dispatchers.IO) {
        productDao.update(product.toEntity())
        
        if (syncManager.hasInternetConnection()) {
            firebaseProductDataSource.updateProduct(product)
        }
    }

    override suspend fun deleteProduct(product: Product) = withContext(Dispatchers.IO) {
        productDao.delete(product.toEntity())
        
        if (syncManager.hasInternetConnection()) {
            firebaseProductDataSource.deleteProduct(product)
        }
    }
}
