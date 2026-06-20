package com.example.quickorderapp.data.remote.firebase

import com.example.quickorderapp.data.local.dao.ProductDao
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
                "timestamp" to System.currentTimeMillis()
            )
            firestore.collection(COLLECTION_PRODUCTS)
                .document(product.nombre) // Usar nombre como ID para simplificar sync
                .set(productMap, SetOptions.merge())
                .await()
            1L
        } catch (e: Exception) {
            0L
        }
    }

    suspend fun updateProduct(product: Product) {
        addProduct(product) // Firestore .set(merge=true) hace lo mismo
    }

    suspend fun deleteProduct(product: Product) {
        try {
            firestore.collection(COLLECTION_PRODUCTS)
                .document(product.nombre)
                .delete()
                .await()
        } catch (e: Exception) { }
    }

    suspend fun syncAllFromCloud() {
        try {
            val snapshot = firestore.collection(COLLECTION_PRODUCTS).get().await()
            val productsFromCloud = snapshot.documents.mapNotNull { doc ->
                Product(
                    id = doc.id.hashCode(),
                    nombre = doc.getString("nombre") ?: "",
                    descripcion = doc.getString("descripcion") ?: "",
                    precio = doc.getDouble("precio") ?: 0.0,
                    imagenUrl = doc.getString("imagenUrl") ?: "",
                    categoria = doc.getString("categoria") ?: "",
                    descuento = doc.getDouble("descuento") ?: 0.0,
                    esPromocion = doc.getBoolean("esPromocion") ?: false
                )
            }
            // Actualizar Room con los datos de la nube
            if (productsFromCloud.isNotEmpty()) {
                productDao.insertAll(productsFromCloud.map { it.toEntity() })
            }
        } catch (e: Exception) { }
    }
}
