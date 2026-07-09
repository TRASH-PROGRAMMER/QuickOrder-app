package com.example.quickorderapp.data.remote.firebase

import com.example.quickorderapp.data.local.dao.ProductDao
import com.example.quickorderapp.data.local.entities.ProductEntity
import com.example.quickorderapp.data.repository.toEntity
import com.example.quickorderapp.domain.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseProductDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val productDao: ProductDao
) {
    companion object {
        private const val COLLECTION_PRODUCTS = "productos"
    }

    suspend fun addProduct(product: Product): Long {
        return try {
            val productMap = hashMapOf(
                "nombre" to product.nombre,
                "descripcion" to product.descripcion,
                "precio" to product.precio,
                "imagenUrl" to product.imagenUrl,
                "categoria" to product.categoria,
                "descuento" to product.descuento,
                "esPromocion" to product.esPromocion,
                "ultimoCambio" to product.ultimoCambio
            )
            firestore.collection(COLLECTION_PRODUCTS)
                .document(product.nombre)
                .set(productMap, SetOptions.merge())
                .await()
            1L
        } catch (e: Exception) {
            0L
        }
    }

    suspend fun updateProduct(product: Product) {
        addProduct(product)
    }

    suspend fun deleteProduct(product: Product) {
        try {
            firestore.collection(COLLECTION_PRODUCTS)
                .document(product.nombre)
                .delete()
                .await()
        } catch (e: Exception) { }
    }

    /**
     * Sincroniza datos de la nube a la base de datos local respetando los cambios más recientes.
     */
    suspend fun syncAllFromCloud() {
        try {
            val snapshot = firestore.collection(COLLECTION_PRODUCTS).get().await()
            val entitiesToUpdate = mutableListOf<ProductEntity>()
            
            for (doc in snapshot.documents) {
                val nombre = doc.getString("nombre") ?: continue
                val cloudUltimoCambio = doc.getLong("ultimoCambio") ?: 0L
                val localProduct = productDao.getByNombre(nombre)
                
                // Estrategia: Solo actualizar si la nube es estrictamente más reciente
                if (localProduct == null || cloudUltimoCambio > localProduct.ultimoCambio) {
                    val product = Product(
                        id = localProduct?.id ?: 0,
                        nombre = nombre,
                        descripcion = doc.getString("descripcion") ?: "",
                        precio = doc.getDouble("precio") ?: 0.0,
                        imagenUrl = doc.getString("imagenUrl") ?: "",
                        categoria = doc.getString("categoria") ?: "",
                        descuento = doc.getDouble("descuento") ?: 0.0,
                        esPromocion = doc.getBoolean("esPromocion") ?: false,
                        ultimoCambio = cloudUltimoCambio
                    )
                    entitiesToUpdate.add(product.toEntity())
                }
            }
            
            if (entitiesToUpdate.isNotEmpty()) {
                productDao.insertAll(entitiesToUpdate)
            }
        } catch (e: Exception) { }
    }
}
