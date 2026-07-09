package com.example.quickorderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickorderapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val role: String = "",
    val isLoading: Boolean = false,
    val passwordChanged: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        combine(
            authRepository.getUserName(),
            authRepository.getUserEmail(),
            authRepository.getUserRole()
        ) { name, email, role ->
            ProfileUiState(name = name, email = email, role = role)
        }.onEach { newState ->
            _uiState.update { newState }
        }.launchIn(viewModelScope)
    }

    fun changePassword(newPassword: String, confirmPassword: String) {
        if (newPassword != confirmPassword) {
            _uiState.update { it.copy(error = "Las contraseñas no coinciden") }
            return
        }
        
        if (newPassword.length < 8) {
            _uiState.update { it.copy(error = "Mínimo 8 caracteres") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val success = authRepository.changePassword(newPassword)
            if (success) {
                _uiState.update { it.copy(isLoading = false, passwordChanged = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "No se pudo cambiar la contraseña") }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(passwordChanged = false, error = null) }
    }
}
