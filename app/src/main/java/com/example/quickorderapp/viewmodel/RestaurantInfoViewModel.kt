package com.example.quickorderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickorderapp.domain.model.RestaurantInfo
import com.example.quickorderapp.domain.repository.AdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RestaurantInfoUiState(
    val info: RestaurantInfo = RestaurantInfo(),
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RestaurantInfoViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RestaurantInfoUiState())
    val uiState: StateFlow<RestaurantInfoUiState> = _uiState.asStateFlow()

    init {
        loadInfo()
    }

    private fun loadInfo() {
        adminRepository.getRestaurantInfo()
            .onStart { _uiState.update { it.copy(isLoading = true) } }
            .onEach { info ->
                if (info != null) {
                    _uiState.update { it.copy(info = info, isLoading = false) }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
            .catch { e ->
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    fun saveInfo(info: RestaurantInfo) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, success = false, error = null) }
                adminRepository.saveRestaurantInfo(info)
                _uiState.update { it.copy(isLoading = false, success = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Fallo al guardar: ${e.message}") }
            }
        }
    }
    
    fun resetSuccess() {
        _uiState.update { it.copy(success = false) }
    }
}
