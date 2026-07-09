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
        userDao.insert(user.toEntity())
        firebaseAuthDataSource.registerUser(user)
        1L
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
}
