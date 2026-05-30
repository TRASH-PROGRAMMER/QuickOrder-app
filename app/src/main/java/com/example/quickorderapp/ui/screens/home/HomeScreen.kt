package com.example.quickorderapp.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

/**

 * Función componible que representa la pantalla de inicio de la aplicación.

 * Esta pantalla sirve como página de inicio principal para el usuario.

 *
 * @param navController El [NavController] utilizado para gestionar las transiciones de navegación entre pantallas.

 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeScreen(navController: NavController) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Home Screen")
    }
}