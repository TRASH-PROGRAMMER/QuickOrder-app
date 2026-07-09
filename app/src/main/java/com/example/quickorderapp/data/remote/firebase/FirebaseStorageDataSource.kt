package com.example.quickorderapp.data.remote.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseStorageDataSource @Inject constructor(
    private val storage: FirebaseStorage
) {
    companion object {
        private const val TAG = "FirebaseStorageDS"
    }

    /**
     * Sube una imagen de producto a Firebase Storage y devuelve la URL de descarga.
     */
    suspend fun uploadImage(uri: Uri): String? {
        return try {
            val fileName = "productos/${System.currentTimeMillis()}.jpg"
            val storageRef = storage.reference.child(fileName)
            
            storageRef.putFile(uri).await()
            storageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e(TAG, "Error uploading image to Firebase Storage: ${e.message}", e)
            null
        }
    }
}
