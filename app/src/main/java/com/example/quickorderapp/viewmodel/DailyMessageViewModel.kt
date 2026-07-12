package com.example.quickorderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickorderapp.domain.model.DailyMessage
import com.example.quickorderapp.domain.repository.AdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class DailyMessageUiState(
    val messages: List<DailyMessage> = emptyList(),
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class DailyMessageViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailyMessageUiState())
    val uiState: StateFlow<DailyMessageUiState> = _uiState.asStateFlow()

    init {
        loadMessages()
    }

    private fun loadMessages() {
        adminRepository.getDailyMessages()
            .onStart { _uiState.update { it.copy(isLoading = true) } }
            .onEach { messages ->
                _uiState.update { it.copy(messages = messages, isLoading = false) }
            }
            .catch { e ->
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    fun saveMessage(titulo: String, mensaje: String, estado: Boolean) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, success = false) }
            val dailyMessage = DailyMessage(
                id = UUID.randomUUID().toString(),
                titulo = titulo,
                mensaje = mensaje,
                estado = estado
            )
            adminRepository.saveDailyMessage(dailyMessage)
            _uiState.update { it.copy(isLoading = false, success = true) }
        }
    }

    fun resetSuccess() {
        _uiState.update { it.copy(success = false) }
    }

    fun deleteMessage(message: DailyMessage) {
        viewModelScope.launch {
            adminRepository.deleteDailyMessage(message)
        }
    }
}
