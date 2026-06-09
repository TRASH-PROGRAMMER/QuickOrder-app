package com.example.quickorderapp.domain.usecase

import com.example.quickorderapp.domain.model.User
import com.example.quickorderapp.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(user: User) = repository.registerUser(user)
}
