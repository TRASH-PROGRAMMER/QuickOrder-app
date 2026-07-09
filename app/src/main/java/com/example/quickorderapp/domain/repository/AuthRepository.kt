package com.example.quickorderapp.domain.repository

import com.example.quickorderapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun registerUser(user: User): Long
    suspend fun login(email: String, password: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun saveSession(user: User)
    fun getUserRole(): Flow<String>
    fun getUserName(): Flow<String>
    fun getUserEmail(): Flow<String>
    suspend fun logout()
    suspend fun changePassword(newPassword: String): Boolean
}
