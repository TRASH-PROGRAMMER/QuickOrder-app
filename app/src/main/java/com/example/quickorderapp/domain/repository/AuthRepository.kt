package com.example.quickorderapp.domain.repository

import com.example.quickorderapp.domain.model.User

interface AuthRepository {
    suspend fun registerUser(user: User): Long
    suspend fun login(email: String, password: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun saveSessionRole(role: String)
    suspend fun logout()
}
