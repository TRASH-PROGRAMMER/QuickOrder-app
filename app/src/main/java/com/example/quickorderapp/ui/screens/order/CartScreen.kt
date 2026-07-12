@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.quickorderapp.ui.screens.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.quickorderapp.R
import com.example.quickorderapp.viewmodel.OrderActionStatus
import com.example.quickorderapp.viewmodel.OrderViewModel
import androidx.compose.foundation.layout.FlowRow

@Composable
fun CartScreen(
    navController: NavController,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val cartState by viewModel.cartState.collectAsStateWithLifecycle()
    val mesas by viewModel.mesas.collectAsStateWithLifecycle()
    val status by viewModel.orderActionStatus.collectAsStateWithLifecycle()

    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(status) {
        if (status is OrderActionStatus.Success) {
            showSuccessDialog = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Pedido") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        if (cartState.items.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Tu carrito está vacío")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cartState.items) { item ->
                        CartItemRow(
                            item = item,
                            onIncrease = { viewModel.updateQuantity(item.productoId, 1) },
                            onDecrease = { viewModel.updateQuantity(item.productoId, -1) }
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Selección de Mesa
                    Text("Selecciona tu Mesa", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        mesas.forEach { mesa ->
                            FilterChip(
                                selected = cartState.mesaSeleccionada == mesa.numero,
                                onClick = { viewModel.setMesa(mesa.numero) },
                                label = { Text("Mesa ${mesa.numero}") }
                            )
                        }
                    }

                    // Notas
                    OutlinedTextField(
                        value = cartState.notas,
                        onValueChange = { viewModel.setNotas(it) },
                        label = { Text("Notas para la cocina (Ej: Sin cebolla)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Total
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text("$${String.format("%.2f", cartState.total)}", style = MaterialTheme.typography.headlineSmall, color = colorResource(R.color.esmeralda), fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { viewModel.confirmOrder() },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        enabled = cartState.mesaSeleccionada != null && status !is OrderActionStatus.Loading,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.esmeralda))
                    ) {
                        if (status is OrderActionStatus.Loading) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Confirmar Pedido", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (showSuccessDialog) {
        val order = (status as? OrderActionStatus.Success)?.order
        AlertDialog(
            onDismissRequest = { 
                showSuccessDialog = false
                viewModel.resetActionStatus()
                navController.popBackStack()
            },
            title = { Text("✔ Pedido confirmado") },
            text = {
                Column {
                    Text("Tu pedido ha sido recibido correctamente.")
                    Text("Nuestro equipo comenzará la preparación en unos momentos.")
                    Spacer(modifier = Modifier.height(8.dp))
                    order?.let {
                        Text("Pedido #: ${it.orderNumber}", fontWeight = FontWeight.Bold)
                        Text("Mesa: ${it.numeroMesa}")
                        Text("Total: $${String.format("%.2f", it.total)}")
                    }
                }
            },
            confirmButton = {
                Button(onClick = { 
                    showSuccessDialog = false
                    viewModel.resetActionStatus()
                    navController.popBackStack()
                }) { Text("Aceptar") }
            }
        )
    }
}

@Composable
fun CartItemRow(item: com.example.quickorderapp.domain.model.OrderItem, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.productoNombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("$${item.precioUnitario} x ${item.cantidad}", style = MaterialTheme.typography.bodyMedium)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecrease, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Remove, contentDescription = null, tint = colorResource(R.color.esmeralda))
                }
                Text("${item.cantidad}", modifier = Modifier.padding(horizontal = 8.dp), fontWeight = FontWeight.Bold)
                IconButton(onClick = onIncrease, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = colorResource(R.color.esmeralda))
                }
            }
        }
    }
}
