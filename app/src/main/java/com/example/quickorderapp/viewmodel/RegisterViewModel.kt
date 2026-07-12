package com.example.quickorderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickorderapp.domain.model.User
import com.example.quickorderapp.domain.usecase.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface RegisterUiState {
    data object Idle : RegisterUiState
    data object Loading : RegisterUiState
    data object Success : RegisterUiState
    data class Error(val message: String) : RegisterUiState
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(user: User, onSuccess: () -> Unit) {
        if (_uiState.value is RegisterUiState.Loading) return
        
        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading
            try {
                val result = registerUserUseCase(user)
                if (result > 0) {
                    _uiState.value = RegisterUiState.Success
                    onSuccess()
                } else {
                    _uiState.value = RegisterUiState.Error("Error al crear la cuenta localmente")
                }
            } catch (e: Exception) {
                _uiState.value = RegisterUiState.Error("Error: ${e.localizedMessage}")
            }
        }
    }
}
