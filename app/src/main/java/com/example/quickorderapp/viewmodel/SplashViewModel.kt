package com.example.quickorderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickorderapp.data.datastore.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de Splash.
 * Cumple con la arquitectura MVVM y las reglas de StateFlow/UiState.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val settings: SettingsDataStore
) : ViewModel() {

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady

    private val _startDestination = MutableStateFlow("login")
    val startDestination: StateFlow<String> = _startDestination

    init {
        viewModelScope.launch {
            delay(1500) // Simulación de carga inicial
            
            // Verificación de configuración inicial
            val isDark = settings.isDarkMode.first()
            
            // Simulación de validación de sesión
            val isLoggedIn = false
            _startDestination.value = if (isLoggedIn) "home" else "login"

            _isReady.value = true
        }
    }
}
