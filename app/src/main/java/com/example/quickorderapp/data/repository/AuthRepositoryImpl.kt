package com.example.quickorderapp.data.repository

import com.example.quickorderapp.data.local.dao.UserDao
import com.example.quickorderapp.data.datastore.SessionManager
import com.example.quickorderapp.data.remote.firebase.FirebaseAuthDataSource
import com.example.quickorderapp.domain.model.User
import com.example.quickorderapp.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val sessionManager: SessionManager,
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun registerUser(user: User): Long = withContext(Dispatchers.IO) {
        try {
            // Primero aseguramos el registro local (Prioridad Etapa 3 - SSOT)
            userDao.insert(user.toEntity())
            
            // Guardamos la sesión localmente para que el usuario pueda operar de inmediato
            saveSession(user)
            
            // Intentamos sincronizar con Firebase, pero no bloqueamos si falla (Resiliencia)
            try {
                firebaseAuthDataSource.registerUser(user)
            } catch (e: Exception) {
                // Logueamos pero permitimos continuar
            }
            
            1L // Éxito local confirmado
        } catch (e: Exception) {
            0L // Fallo crítico en BD local
        }
    }

    override suspend fun login(email: String, password: String): User? = withContext(Dispatchers.IO) {
        val cloudUser = firebaseAuthDataSource.login(email, password)
        
        if (cloudUser != null) {
            userDao.insert(cloudUser.copy(password = password).toEntity())
            saveSession(cloudUser)
            return@withContext cloudUser
        }

        val localUser = userDao.getUserByEmail(email)?.toDomain()
        if (localUser != null && localUser.password == password) {
            saveSession(localUser)
            return@withContext localUser
        }
        
        null
    }

    override suspend fun getUserByEmail(email: String): User? = withContext(Dispatchers.IO) {
        userDao.getUserByEmail(email)?.toDomain()
    }

    override suspend fun saveSession(user: User) = withContext(Dispatchers.IO) {
        sessionManager.saveSession(user.nombre, user.correo, user.rol)
    }

    override fun getUserRole(): Flow<String> = sessionManager.userRole
    override fun getUserName(): Flow<String> = sessionManager.userName
    override fun getUserEmail(): Flow<String> = sessionManager.userEmail

    override suspend fun logout() = withContext(Dispatchers.IO) {
        firebaseAuthDataSource.logout()
        sessionManager.clearSession()
    }

    override suspend fun changePassword(newPassword: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val user = firebaseAuth.currentUser
            user?.updatePassword(newPassword)?.await()
            // También deberíamos actualizarlo localmente si quisiéramos mantener paridad offline
            // pero Firebase Auth es la fuente primaria para el password real.
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun updateProfile(nombre: String, correo: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val firebaseUser = firebaseAuth.currentUser ?: return@withContext false
            
            // 1. Actualizar en Firestore
            firebaseAuthDataSource.updateUserProfile(firebaseUser.uid, nombre, correo)
            
            // 2. Actualizar en Room
            val localUser = userDao.getUserByEmail(firebaseUser.email ?: "")
            if (localUser != null) {
                userDao.insert(localUser.copy(nombre = nombre, correo = correo))
            }
            
            // 3. Actualizar sesión
            sessionManager.saveSession(nombre, correo, localUser?.rol ?: "COMENSAL")

            true
        } catch (e: Exception) {
            false
        }
    }
}
