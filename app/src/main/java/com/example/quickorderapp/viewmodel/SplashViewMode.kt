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

/**

 * ViewModel responsable de gestionar la lógica de la pantalla de inicio.

 *
 * Este ViewModel se encarga de la configuración inicial de la aplicación, como la comprobación de las preferencias del usuario,

 * la simulación de un retardo de inicio y la determinación del destino de navegación inicial
 * (por ejemplo, "inicio" o "inicio de sesión") en función del estado de autenticación del usuario.

 *
 * @property settings El [SettingsDataStore] utilizado para recuperar las preferencias del usuario, como el modo oscuro.

 * @property isReady Un [StateFlow] que indica si la aplicación ha finalizado su

 * carga inicial y está lista para salir de la pantalla de inicio.

 * @property startDestination Un [StateFlow] que contiene el ID de ruta para la primera pantalla

 * que se mostrará después de que finalice el proceso de la pantalla de inicio.

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
            delay(1500) // Simula carga

            val isDark = settings.isDarkMode.first()

            // Aquí validar sesión
            // Ejemplo:
            val isLoggedIn = false

            _startDestination.value = if (isLoggedIn) "home" else "login"

            _isReady.value = true
        }
    }
}
