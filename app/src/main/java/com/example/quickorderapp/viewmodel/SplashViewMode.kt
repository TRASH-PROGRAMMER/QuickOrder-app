package com.example.quickorderapp.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickorderapp.data.local.datastore.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

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
            delay(1500) // Simula carga

            val isDark = settings.isDarkMode.first()

            // Aquí podrías validar sesión
            // Ejemplo:
            val isLoggedIn = false

            _startDestination.value = if (isLoggedIn) "home" else "login"

            _isReady.value = true
        }
    }
}
