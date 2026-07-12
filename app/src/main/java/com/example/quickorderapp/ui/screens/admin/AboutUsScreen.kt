@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.quickorderapp.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quickorderapp.R
import com.example.quickorderapp.domain.model.RestaurantInfo
import com.example.quickorderapp.viewmodel.RestaurantInfoViewModel
import kotlinx.coroutines.launch

@Composable
fun AboutUsScreen(
    viewModel: RestaurantInfoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var info by remember { mutableStateOf(RestaurantInfo()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.info) {
        info = uiState.info
    }

    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            scope.launch {
                snackbarHostState.showSnackbar("Información actualizada correctamente")
            }
            viewModel.resetSuccess()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Sobre Nosotros", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { /* NavController back if needed, but this is a subscreen */ }) {
                        // Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InfoSection(title = "General", icon = Icons.Default.Restaurant) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(value = info.nombre, onValueChange = { info = info.copy(nombre = it) }, label = { Text("Nombre del Restaurante") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                    OutlinedTextField(value = info.descripcion, onValueChange = { info = info.copy(descripcion = it) }, label = { Text("Descripción Corta") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                }
            }

            InfoSection(title = "Historia", icon = Icons.Default.History) {
                OutlinedTextField(value = info.historia, onValueChange = { info = info.copy(historia = it) }, label = { Text("Nuestra Historia") }, modifier = Modifier.fillMaxWidth(), minLines = 4, shape = RoundedCornerShape(12.dp))
            }

            InfoSection(title = "Contacto y Horarios", icon = Icons.Default.ContactPage) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(value = info.direccion, onValueChange = { info = info.copy(direccion = it) }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), leadingIcon = { Icon(Icons.Default.LocationOn, null) })
                    OutlinedTextField(value = info.telefono, onValueChange = { info = info.copy(telefono = it) }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), leadingIcon = { Icon(Icons.Default.Phone, null) })
                    OutlinedTextField(value = info.correo, onValueChange = { info = info.copy(correo = it) }, label = { Text("Correo Electrónico") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), leadingIcon = { Icon(Icons.Default.Email, null) })
                    OutlinedTextField(value = info.horario, onValueChange = { info = info.copy(horario = it) }, label = { Text("Horario de Atención") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), leadingIcon = { Icon(Icons.Default.Schedule, null) })
                }
            }

            Button(
                onClick = { viewModel.saveInfo(info) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.esmeralda)),
                shape = RoundedCornerShape(16.dp),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                else Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Save, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Guardar Cambios", fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun InfoSection(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, content: @Composable () -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
            Icon(icon, null, tint = colorResource(R.color.esmeralda), modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        content()
    }
}
