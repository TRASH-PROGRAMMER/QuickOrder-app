package com.example.quickorderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickorderapp.domain.model.User
import com.example.quickorderapp.domain.usecase.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    fun register(user: User, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                registerUserUseCase(user)
                onSuccess()
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
}
