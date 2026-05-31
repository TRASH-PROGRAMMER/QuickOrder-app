package com.example.quickorderapp.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quickorderapp.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text


/**

 * Función componible que representa la pantalla de inicio de la aplicación.

 * Esta pantalla sirve como página de inicio principal para el usuario.

 *
 * @param navController El [NavController] utilizado para gestionar las transiciones de navegación entre pantallas.

 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),// Agrega padding
        verticalArrangement = Arrangement.Top
    ){
        Text("MENU",
            style = MaterialTheme.typography.headlineMedium,
            textAlign=TextAlign.Left ,
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp) ,
            fontWeight = FontWeight.Bold, fontFamily=FontFamily.SansSerif,
        )
        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp),
            thickness = 2.dp,
            color = colorResource(id = R.color.gray)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            FilterChip(
                selected = true,
                onClick = { },
                label = { Text("Entradas") }
            )
            Spacer(modifier = Modifier.width(8.dp))

            FilterChip(
                selected = false,
                onClick = { },
                label = { Text("Platos Fuertes") }
            )

            Spacer(modifier = Modifier.width(8.dp))

            FilterChip(
                selected = false,
                onClick = { },
                label = { Text("Promociones") }
            )
        }
        }
    }
