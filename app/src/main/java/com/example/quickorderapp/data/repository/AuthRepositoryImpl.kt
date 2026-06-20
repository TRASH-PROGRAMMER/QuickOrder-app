package com.example.quickorderapp.data.repository

import com.example.quickorderapp.data.local.dao.UserDao
import com.example.quickorderapp.data.datastore.SessionManager
import com.example.quickorderapp.data.remote.firebase.FirebaseAuthDataSource
import com.example.quickorderapp.domain.model.User
import com.example.quickorderapp.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val sessionManager: SessionManager,
    private val firebaseAuthDataSource: FirebaseAuthDataSource
) : AuthRepository {

    override suspend fun registerUser(user: User): Long = withContext(Dispatchers.IO) {
        // Guardar localmente primero (offline-first)
        userDao.insert(user.toEntity())
        
        // Intentar sincronizar con Firebase si hay conexión
        try {
            firebaseAuthDataSource.registerUser(user)
        } catch (e: Exception) {
            // Silenciar error - los datos están en Room
        }
        
        1L
    }

    override suspend fun getUserByEmail(email: String): User? = withContext(Dispatchers.IO) {
        // Fallback a Room local
        userDao.getUserByEmail(email)?.toDomain()
    }

    override suspend fun saveSessionRole(role: String) = withContext(Dispatchers.IO) {
        sessionManager.saveRole(role)
    }
}
