package com.example.quickorderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickorderapp.domain.model.Mesa
import com.example.quickorderapp.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MesaUiState {
    data object Loading : MesaUiState
    data class Success(val mesas: List<Mesa>) : MesaUiState
    data class Error(val message: String) : MesaUiState
}

@HiltViewModel
class MesaViewModel @Inject constructor(
    private val getMesasUseCase: GetMesasUseCase,
    private val addMesaUseCase: AddMesaUseCase,
    private val updateMesaUseCase: UpdateMesaUseCase,
    private val deleteMesaUseCase: DeleteMesaUseCase
) : ViewModel() {

    val uiState: StateFlow<MesaUiState> = getMesasUseCase()
        .map<List<Mesa>, MesaUiState> { mesas -> 
            MesaUiState.Success(mesas) 
        }
        .catch { e -> 
            emit(MesaUiState.Error("Error al cargar mesas: ${e.localizedMessage}")) 
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MesaUiState.Loading
        )

    fun addMesa(mesa: Mesa) {
        viewModelScope.launch {
            try {
                addMesaUseCase(mesa)
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    fun updateMesa(mesa: Mesa) {
        viewModelScope.launch {
            try {
                updateMesaUseCase(mesa)
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    fun deleteMesa(mesa: Mesa) {
        viewModelScope.launch {
            try {
                deleteMesaUseCase(mesa)
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
}
