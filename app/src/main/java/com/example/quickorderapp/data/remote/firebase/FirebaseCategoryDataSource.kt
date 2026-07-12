package com.example.quickorderapp.data.remote.firebase

import android.util.Log
import com.example.quickorderapp.data.local.dao.CategoryDao
import com.example.quickorderapp.data.repository.toEntity
import com.example.quickorderapp.domain.model.Category
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseCategoryDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val categoryDao: CategoryDao
) {
    companion object {
        private const val TAG = "FirebaseCategoryDS"
        private const val COLLECTION_CATEGORIES = "categories"
    }

    suspend fun addCategory(category: Category) {
        try {
            val categoryMap = hashMapOf(
                "id" to category.id,
                "nombre" to category.nombre,
                "estado" to category.estado,
                "fechaCreacion" to category.fechaCreacion
            )
            firestore.collection(COLLECTION_CATEGORIES)
                .document(category.id)
                .set(categoryMap, SetOptions.merge())
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Error adding category to Firebase", e)
        }
    }

    suspend fun deleteCategory(category: Category) {
        try {
            firestore.collection(COLLECTION_CATEGORIES)
                .document(category.id)
                .delete()
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting category from Firebase", e)
        }
    }

    suspend fun syncAllFromCloud() {
        try {
            val snapshot = firestore.collection(COLLECTION_CATEGORIES).get().await()
            val categoriesFromCloud = snapshot.documents.mapNotNull { doc ->
                Category(
                    id = doc.getString("id") ?: "",
                    nombre = doc.getString("nombre") ?: "",
                    estado = doc.getBoolean("estado") ?: true,
                    fechaCreacion = doc.getLong("fechaCreacion") ?: 0L
                )
            }
            categoriesFromCloud.forEach {
                categoryDao.insert(it.toEntity())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing categories from Cloud", e)
        }
    }
}
