package com.example.quickorderapp.data.repository

import com.example.quickorderapp.data.local.dao.UserDao
import com.example.quickorderapp.data.datastore.SessionManager
import com.example.quickorderapp.domain.model.User
import com.example.quickorderapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun registerUser(user: User): Long {
        userDao.insert(user.toEntity())
        return 1L // Simplificado
    }

    override suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)?.toDomain()
    }

    override suspend fun saveSessionRole(role: String) {
        sessionManager.saveRole(role)
    }
}
