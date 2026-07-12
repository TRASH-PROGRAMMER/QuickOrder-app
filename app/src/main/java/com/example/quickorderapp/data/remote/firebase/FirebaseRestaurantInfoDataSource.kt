package com.example.quickorderapp.data.remote.firebase

import android.util.Log
import com.example.quickorderapp.data.local.dao.RestaurantInfoDao
import com.example.quickorderapp.data.repository.toEntity
import com.example.quickorderapp.domain.model.RestaurantInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRestaurantInfoDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val restaurantInfoDao: RestaurantInfoDao
) {
    companion object {
        private const val TAG = "FirebaseRestInfoDS"
        private const val COLLECTION_INFO = "restaurant_info"
        private const val DOCUMENT_ID = "rest_info"
    }

    suspend fun saveInfo(info: RestaurantInfo) {
        try {
            val infoMap = hashMapOf(
                "nombre" to info.nombre,
                "historia" to info.historia,
                "descripcion" to info.descripcion,
                "direccion" to info.direccion,
                "telefono" to info.telefono,
                "correo" to info.correo,
                "horario" to info.horario,
                "redesSociales" to info.redesSociales
            )
            firestore.collection(COLLECTION_INFO)
                .document(DOCUMENT_ID)
                .set(infoMap, SetOptions.merge())
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Error saving restaurant info to Firebase", e)
        }
    }

    suspend fun syncFromCloud() {
        try {
            val doc = firestore.collection(COLLECTION_INFO).document(DOCUMENT_ID).get().await()
            if (doc.exists()) {
                val info = RestaurantInfo(
                    id = DOCUMENT_ID,
                    nombre = doc.getString("nombre") ?: "",
                    historia = doc.getString("historia") ?: "",
                    descripcion = doc.getString("descripcion") ?: "",
                    direccion = doc.getString("direccion") ?: "",
                    telefono = doc.getString("telefono") ?: "",
                    correo = doc.getString("correo") ?: "",
                    horario = doc.getString("horario") ?: "",
                    redesSociales = doc.getString("redesSociales") ?: ""
                )
                restaurantInfoDao.insert(info.toEntity())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing restaurant info from Cloud", e)
        }
    }
}
