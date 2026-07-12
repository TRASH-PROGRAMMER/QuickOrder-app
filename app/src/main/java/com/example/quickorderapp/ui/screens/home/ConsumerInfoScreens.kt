package com.example.quickorderapp.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quickorderapp.R
import com.example.quickorderapp.viewmodel.DailyMessageViewModel
import com.example.quickorderapp.viewmodel.RestaurantInfoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyMessageConsumerScreen(
    viewModel: DailyMessageViewModel = hiltViewModel(),
    onOpenDrawer: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { 
            CenterAlignedTopAppBar(
                title = { Text("Mensaje del Día") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            ) 
        }
    ) { padding ->
        if (uiState.messages.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("No hay mensajes nuevos hoy")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.messages) { msg ->
                    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(msg.titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Text(msg.mensaje)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsConsumerScreen(
    viewModel: RestaurantInfoViewModel = hiltViewModel(),
    onOpenDrawer: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { 
            CenterAlignedTopAppBar(
                title = { Text("Sobre Nosotros") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            ) 
        }
    ) { padding ->
        val info = uiState.info
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                info.nombre.ifEmpty { "QuickOrder" }, 
                style = MaterialTheme.typography.headlineMedium, 
                fontWeight = FontWeight.Bold, 
                color = colorResource(R.color.esmeralda)
            )
            Text(info.descripcion.ifEmpty { "Tu restaurante favorito." }, style = MaterialTheme.typography.bodyLarge)
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            Text("Nuestra Historia", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(info.historia.ifEmpty { "Sirviendo con amor desde siempre." })
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            Text("Contacto", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("📍 Dirección: ${info.direccion}")
            Text("📞 Teléfono: ${info.telefono}")
            Text("📧 Correo: ${info.correo}")
            Text("🕒 Horario: ${info.horario}")
        }
    }
}
