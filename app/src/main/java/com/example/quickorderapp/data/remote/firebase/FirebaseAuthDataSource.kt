package com.example.quickorderapp.data.remote.firebase

import android.util.Log
import com.example.quickorderapp.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * DataSource remoto para autenticación y usuarios usando Firestore.
 */
@Singleton
class FirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    companion object {
        private const val TAG = "FirebaseAuthDS"
        private const val COLLECTION_USERS = "usuarios"
    }

    suspend fun login(email: String, password: String): User? {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val uid = firebaseAuth.currentUser?.uid ?: return null
            getUserFromFirestore(uid)
        } catch (e: Exception) {
            Log.e(TAG, "Error during Firebase login: ${e.message}", e)
            null
        }
    }

    suspend fun registerUser(user: User): Long {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(
                user.correo, 
                user.password
            ).await()
            
            authResult.user?.let { firebaseUser ->
                val userMap = hashMapOf(
                    "nombre" to user.nombre,
                    "correo" to user.correo,
                    "rol" to user.rol,
                    "timestamp" to System.currentTimeMillis()
                )
                firestore.collection(COLLECTION_USERS)
                    .document(firebaseUser.uid)
                    .set(userMap)
                    .await()
            }
            1L
        } catch (e: Exception) {
            Log.e(TAG, "Error during Firebase registration: ${e.message}", e)
            0L
        }
    }

    private suspend fun getUserFromFirestore(uid: String): User? {
        return try {
            val document = firestore.collection(COLLECTION_USERS)
                .document(uid)
                .get()
                .await()
            
            if (document.exists()) {
                User(
                    id = document.id.hashCode(),
                    nombre = document.getString("nombre") ?: "",
                    correo = document.getString("correo") ?: "",
                    rol = document.getString("rol") ?: "MESERO",
                    password = ""
                )
            } else null
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching user from Firestore: ${e.message}", e)
            null
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun getCurrentUserId(): String? = firebaseAuth.currentUser?.uid

    suspend fun updateUserProfile(uid: String, nombre: String, correo: String) {
        try {
            val updates = hashMapOf(
                "nombre" to nombre,
                "correo" to correo
            )
            firestore.collection(COLLECTION_USERS)
                .document(uid)
                .update(updates as Map<String, Any>)
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Error updating user profile in Firestore: ${e.message}", e)
        }
    }
}
