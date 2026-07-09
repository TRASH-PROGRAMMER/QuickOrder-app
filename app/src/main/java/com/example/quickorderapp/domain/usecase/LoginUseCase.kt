package com.example.quickorderapp.domain.usecase

import com.example.quickorderapp.domain.model.User
import com.example.quickorderapp.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): User? {
        // Delegamos la autenticación al repositorio (que maneja Firebase + Room)
        val user = repository.login(email, password)
        
        if (user != null) {
            repository.saveSessionRole(user.rol)
        }
        
        return user
    }
}
