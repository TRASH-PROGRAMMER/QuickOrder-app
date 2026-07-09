package com.example.quickorderapp.data.remote.firebase

import android.util.Log
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
        private const val TAG = "FirebaseProductDS"
        private const val COLLECTION_PRODUCTS = "productos"
    }

    suspend fun addProduct(product: Product): Long {
        return try {
            val productMap = hashMapOf(
                "uid" to product.uid,
                "nombre" to product.nombre,
                "descripcion" to product.descripcion,
                "precio" to product.precio,
                "imagenUrl" to product.imagenUrl,
                "categoria" to product.categoria,
                "descuento" to product.descuento,
                "esPromocion" to product.esPromocion,
                "ultimoCambio" to product.ultimoCambio
            )
            // Usamos UID como identificador único en Firestore
            firestore.collection(COLLECTION_PRODUCTS)
                .document(product.uid)
                .set(productMap, SetOptions.merge())
                .await()
            1L
        } catch (e: Exception) {
            Log.e(TAG, "Error adding product to Firebase: ${e.message}", e)
            0L
        }
    }

    suspend fun updateProduct(product: Product) {
        addProduct(product)
    }

    suspend fun deleteProduct(product: Product) {
        try {
            firestore.collection(COLLECTION_PRODUCTS)
                .document(product.uid)
                .delete()
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting product from Firebase: ${e.message}", e)
        }
    }

    /**
     * Sincroniza datos de la nube a la base de datos local respetando los cambios más recientes.
     * Utiliza el UID como clave de sincronización.
     */
    suspend fun syncAllFromCloud() {
        try {
            val snapshot = firestore.collection(COLLECTION_PRODUCTS).get().await()
            val entitiesToUpdate = mutableListOf<ProductEntity>()
            
            for (doc in snapshot.documents) {
                val uid = doc.getString("uid") ?: continue
                val cloudUltimoCambio = doc.getLong("ultimoCambio") ?: 0L
                val localProduct = productDao.getByUid(uid)
                
                // Estrategia: Solo actualizar si la nube es estrictamente más reciente o no existe localmente
                if (localProduct == null || cloudUltimoCambio > localProduct.ultimoCambio) {
                    val product = Product(
                        id = localProduct?.id ?: 0,
                        uid = uid,
                        nombre = doc.getString("nombre") ?: "",
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
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing products from Cloud: ${e.message}", e)
        }
    }
}
