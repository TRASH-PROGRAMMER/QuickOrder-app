package com.example.quickorderapp.data.repository

import android.net.Uri
import com.example.quickorderapp.data.local.LocalImageDataSource
import com.example.quickorderapp.data.local.dao.CategoryDao
import com.example.quickorderapp.data.local.dao.ProductDao
import com.example.quickorderapp.data.remote.firebase.FirebaseProductDataSource
import com.example.quickorderapp.data.remote.firebase.FirebaseStorageDataSource
import com.example.quickorderapp.data.remote.firebase.FirebaseSyncManager
import com.example.quickorderapp.domain.model.Category
import com.example.quickorderapp.domain.model.Product
import com.example.quickorderapp.domain.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao,
    private val firebaseProductDataSource: FirebaseProductDataSource,
    private val storageDataSource: FirebaseStorageDataSource,
    private val localImageDataSource: LocalImageDataSource,
    private val syncManager: FirebaseSyncManager
) : ProductRepository {
    
    override fun getProducts(): Flow<List<Product>> {
        if (syncManager.hasInternetConnection()) {
            syncManager.syncAll()
        }
        return productDao.getAll().map { it.toProductDomainList() }
    }

    override fun getCategories(): Flow<List<Category>> {
        return categoryDao.getAll().map { it.toCategoryDomainList() }
    }

    override suspend fun addProduct(product: Product): Boolean = withContext(Dispatchers.IO) {
        try {
            val productWithTimestamp = product.copy(ultimoCambio = System.currentTimeMillis())
            val toSave = uploadImageIfNeeded(productWithTimestamp)
            productDao.insert(toSave.toEntity())
            if (syncManager.hasInternetConnection()) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        firebaseProductDataSource.addProduct(toSave)
                    } catch (e: Exception) { }
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun updateProduct(product: Product): Boolean = withContext(Dispatchers.IO) {
        try {
            val productWithTimestamp = product.copy(ultimoCambio = System.currentTimeMillis())
            val toSave = uploadImageIfNeeded(productWithTimestamp)
            productDao.update(toSave.toEntity())
            if (syncManager.hasInternetConnection()) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        firebaseProductDataSource.updateProduct(toSave)
                    } catch (e: Exception) { }
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteProduct(product: Product): Boolean = withContext(Dispatchers.IO) {
        try {
            productDao.delete(product.toEntity())
            if (syncManager.hasInternetConnection()) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        firebaseProductDataSource.deleteProduct(product)
                    } catch (e: Exception) { }
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun uploadImageIfNeeded(product: Product): Product {
        val url = product.imagenUrl
        if (!url.startsWith("content://")) return product

        val source = Uri.parse(url)

        // 1) Copia persistente en almacenamiento interno (sobrevive a reinicios)
        val localPath = localImageDataSource.copyToInternalStorage(source) ?: return product

        // 2) Si hay red, sube a Storage y usa la URL canonica para sincronizacion
        return if (syncManager.hasInternetConnection()) {
            val remote = storageDataSource.uploadImage(source)
            if (remote != null) {
                product.copy(imagenUrl = remote)
            } else {
                product.copy(imagenUrl = localPath)
            }
        } else {
            product.copy(imagenUrl = localPath)
        }
    }
}
