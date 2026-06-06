package com.example.quickorderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickorderapp.data.datastore.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    fun login(email: String) {
        viewModelScope.launch {
            // Si el correo contiene "admin", le damos rol de ADMIN para pruebas
            val role = if (email.contains("admin")) "ADMIN" else "MESERO"
            sessionManager.saveRole(role)
        }
    }
}
