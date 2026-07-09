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
        userDao.insert(user.toEntity())
        try {
            firebaseAuthDataSource.registerUser(user)
        } catch (e: Exception) { }
        1L
    }

    override suspend fun login(email: String, password: String): User? = withContext(Dispatchers.IO) {
        // 1. Intentar login real con Firebase
        val cloudUser = firebaseAuthDataSource.login(email, password)
        
        if (cloudUser != null) {
            // Sincronizar con Room local: Guardamos el perfil bajado de la nube
            // Nota: El password no se baja de Firebase por seguridad, pero lo guardamos en Room 
            // para permitir login offline posterior.
            userDao.insert(cloudUser.copy(password = password).toEntity())
            return@withContext cloudUser
        }

        // 2. Fallback Offline: Validar contra Room local
        val localUser = userDao.getUserByEmail(email)?.toDomain()
        if (localUser != null && localUser.password == password) {
            return@withContext localUser
        }
        
        null
    }

    override suspend fun getUserByEmail(email: String): User? = withContext(Dispatchers.IO) {
        userDao.getUserByEmail(email)?.toDomain()
    }

    override suspend fun saveSessionRole(role: String) = withContext(Dispatchers.IO) {
        sessionManager.saveRole(role)
    }

    override suspend fun logout() = withContext(Dispatchers.IO) {
        firebaseAuthDataSource.logout()
        sessionManager.clearSession()
    }
}
