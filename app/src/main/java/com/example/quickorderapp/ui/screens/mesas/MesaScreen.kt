@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.quickorderapp.ui.screens.mesas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import com.example.quickorderapp.domain.model.Mesa
import com.example.quickorderapp.ui.components.MesaCard
import com.example.quickorderapp.viewmodel.HomeViewModel
import com.example.quickorderapp.viewmodel.MesaUiState
import com.example.quickorderapp.viewmodel.MesaViewModel

@Composable
fun MesaScreen(
    mesaViewModel: MesaViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by mesaViewModel.uiState.collectAsStateWithLifecycle()
    val userRole by homeViewModel.userRole.collectAsStateWithLifecycle()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var mesaToEdit by remember { mutableStateOf<Mesa?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    if (showAddDialog) {
        MesaDialog(
            mesa = mesaToEdit,
            onDismiss = { 
                showAddDialog = false
                mesaToEdit = null
            },
            onConfirm = { mesa ->
                if (mesaToEdit == null) mesaViewModel.addMesa(mesa)
                else mesaViewModel.updateMesa(mesa)
                showAddDialog = false
                mesaToEdit = null
            }
        )
    }

    MesaScreenContent(
        uiState = uiState,
        isAdmin = userRole == "ADMIN",
        snackbarHostState = snackbarHostState,
        onAddClick = { showAddDialog = true },
        onEditClick = { 
            mesaToEdit = it
            showAddDialog = true
        },
        onDeleteClick = { mesaViewModel.deleteMesa(it) }
    )
}

@Composable
fun MesaScreenContent(
    uiState: MesaUiState,
    isAdmin: Boolean,
    snackbarHostState: SnackbarHostState,
    onAddClick: () -> Unit,
    onEditClick: (Mesa) -> Unit,
    onDeleteClick: (Mesa) -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Gestión de Mesas", fontWeight = FontWeight.Bold) }
            )
        },
        floatingActionButton = {
            if (isAdmin) {
                FloatingActionButton(
                    onClick = onAddClick,
                    containerColor = colorResource(R.color.esmeralda),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir Mesa", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (uiState) {
                is MesaUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = colorResource(R.color.esmeralda))
                    }
                }
                is MesaUiState.Success -> {
                    if (uiState.mesas.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.TableBar, null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
                                Spacer(Modifier.height(16.dp))
                                Text("No hay mesas registradas", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.mesas) { mesa ->
                                MesaCard(
                                    mesa = mesa,
                                    isAdmin = isAdmin,
                                    onEdit = onEditClick,
                                    onDelete = onDeleteClick
                                )
                            }
                        }
                    }
                }
                is MesaUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(uiState.message, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
fun MesaDialog(
    mesa: Mesa?,
    onDismiss: () -> Unit,
    onConfirm: (Mesa) -> Unit
) {
    var numero by remember { mutableStateOf(mesa?.numero?.toString() ?: "") }
    var capacidad by remember { mutableStateOf(mesa?.capacidad?.toString() ?: "") }
    var estado by remember { mutableStateOf(mesa?.estado ?: "Libre") }
    var observaciones by remember { mutableStateOf(mesa?.observaciones ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (mesa == null) "Nueva Mesa" else "Editar Mesa") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = numero,
                    onValueChange = { if (it.all { c -> c.isDigit() }) numero = it },
                    label = { Text("Número de Mesa") },
                    enabled = mesa == null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.Pin, null) }
                )
                OutlinedTextField(
                    value = capacidad,
                    onValueChange = { if (it.all { c -> c.isDigit() }) capacidad = it },
                    label = { Text("Capacidad") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.Groups, null) }
                )
                OutlinedTextField(
                    value = observaciones,
                    onValueChange = { observaciones = it },
                    label = { Text("Observaciones") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                
                Text("Estado", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Libre", "Ocupada", "Reservada").forEach { est ->
                        FilterChip(
                            selected = estado == est,
                            onClick = { estado = est },
                            label = { Text(est) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        Mesa(
                            numero = numero.toIntOrNull() ?: 0,
                            capacidad = capacidad.toIntOrNull() ?: 0,
                            estado = estado,
                            observaciones = observaciones
                        )
                    )
                },
                enabled = numero.isNotBlank() && capacidad.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.esmeralda)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
