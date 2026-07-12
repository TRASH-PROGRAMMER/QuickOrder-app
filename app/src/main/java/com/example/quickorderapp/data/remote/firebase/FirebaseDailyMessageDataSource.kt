package com.example.quickorderapp.data.remote.firebase

import android.util.Log
import com.example.quickorderapp.data.local.dao.DailyMessageDao
import com.example.quickorderapp.data.repository.toEntity
import com.example.quickorderapp.domain.model.DailyMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseDailyMessageDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val dailyMessageDao: DailyMessageDao
) {
    companion object {
        private const val TAG = "FirebaseDailyMessageDS"
        private const val COLLECTION_MESSAGES = "daily_messages"
    }

    suspend fun saveMessage(message: DailyMessage) {
        try {
            val messageMap = hashMapOf(
                "id" to message.id,
                "titulo" to message.titulo,
                "mensaje" to message.mensaje,
                "fechaPublicacion" to message.fechaPublicacion,
                "estado" to message.estado
            )
            firestore.collection(COLLECTION_MESSAGES)
                .document(message.id)
                .set(messageMap, SetOptions.merge())
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Error saving daily message to Firebase", e)
        }
    }

    suspend fun deleteMessage(message: DailyMessage) {
        try {
            firestore.collection(COLLECTION_MESSAGES)
                .document(message.id)
                .delete()
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting daily message from Firebase", e)
        }
    }

    suspend fun syncAllFromCloud() {
        try {
            val snapshot = firestore.collection(COLLECTION_MESSAGES).get().await()
            val messagesFromCloud = snapshot.documents.mapNotNull { doc ->
                DailyMessage(
                    id = doc.getString("id") ?: "",
                    titulo = doc.getString("titulo") ?: "",
                    mensaje = doc.getString("mensaje") ?: "",
                    fechaPublicacion = doc.getLong("fechaPublicacion") ?: 0L,
                    estado = doc.getBoolean("estado") ?: true
                )
            }
            messagesFromCloud.forEach {
                dailyMessageDao.insert(it.toEntity())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing daily messages from Cloud", e)
        }
    }
}
