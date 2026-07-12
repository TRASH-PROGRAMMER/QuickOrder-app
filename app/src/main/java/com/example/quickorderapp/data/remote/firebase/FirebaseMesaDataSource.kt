package com.example.quickorderapp.data.remote.firebase

import android.util.Log
import com.example.quickorderapp.data.local.dao.MesaDao
import com.example.quickorderapp.data.repository.toEntity
import com.example.quickorderapp.domain.model.Mesa
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseMesaDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val mesaDao: MesaDao
) {
    companion object {
        private const val TAG = "FirebaseMesaDS"
        private const val COLLECTION_MESAS = "mesas"
    }

    suspend fun addMesa(mesa: Mesa) {
        try {
            val mesaMap = hashMapOf(
                "numero" to mesa.numero,
                "capacidad" to mesa.capacidad,
                "estado" to mesa.estado,
                "qrCode" to mesa.qrCode,
                "observaciones" to mesa.observaciones
            )
            firestore.collection(COLLECTION_MESAS)
                .document(mesa.numero.toString())
                .set(mesaMap, SetOptions.merge())
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Error adding mesa to Firebase: ${e.message}", e)
        }
    }

    suspend fun deleteMesa(mesa: Mesa) {
        try {
            firestore.collection(COLLECTION_MESAS)
                .document(mesa.numero.toString())
                .delete()
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting mesa from Firebase: ${e.message}", e)
        }
    }

    suspend fun syncAllFromCloud() {
        try {
            val snapshot = firestore.collection(COLLECTION_MESAS).get().await()
            val mesasFromCloud = snapshot.documents.mapNotNull { doc ->
                Mesa(
                    numero = doc.getLong("numero")?.toInt() ?: 0,
                    capacidad = doc.getLong("capacidad")?.toInt() ?: 0,
                    estado = doc.getString("estado") ?: "Libre",
                    qrCode = doc.getString("qrCode"),
                    observaciones = doc.getString("observaciones") ?: ""
                )
            }
            if (mesasFromCloud.isNotEmpty()) {
                mesaDao.insertAll(mesasFromCloud.map { it.toEntity() })
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing mesas from Cloud: ${e.message}", e)
        }
    }
}
