@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.quickorderapp.ui.screens.mesero

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quickorderapp.R
import com.example.quickorderapp.domain.model.Order
import com.example.quickorderapp.viewmodel.MeseroViewModel
import com.example.quickorderapp.viewmodel.OrderViewModel
import java.text.SimpleDateFormat
import java.util.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.*

@Composable
fun MeseroOrderDetailScreen(
    orderId: Int,
    navController: NavController,
    meseroViewModel: MeseroViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel()
) {
    val order by orderViewModel.getOrderById(orderId).collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Pedido") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        order?.let { o ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
            ) {
                MeseroOrderInfoCard(o)
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Estado Actual: ${o.estado}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                
                // Status Management
                MeseroStatusActions(o) { newStatus ->
                    meseroViewModel.updateOrderStatus(o, newStatus)
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("Productos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(o.items) { item ->
                        ListItem(
                            headlineContent = { Text(item.productoNombre) },
                            supportingContent = { Text("${item.cantidad} x $${item.precioUnitario}") },
                            trailingContent = { Text("$${String.format("%.2f", item.subtotal)}", fontWeight = FontWeight.Bold) }
                        )
                    }
                }
                
                if (o.notas.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Notas:", fontWeight = FontWeight.Bold)
                    Text(o.notas, style = MaterialTheme.typography.bodyMedium)
                }
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun MeseroStatusActions(order: Order, onUpdate: (String) -> Unit) {
    val states = listOf("PENDIENTE", "EN PREPARACIÓN", "COMPLETADO", "CANCELADO")
    
    // Business rules: Pendiente -> Preparando -> Completado. Or Pendiente -> Cancelado.
    val possibleNextStates = when(order.estado) {
        "PENDIENTE" -> listOf("EN PREPARACIÓN", "CANCELADO")
        "EN PREPARACIÓN" -> listOf("COMPLETADO")
        else -> emptyList()
    }

    if (possibleNextStates.isNotEmpty()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            possibleNextStates.forEach { state ->
                Button(
                    onClick = { onUpdate(state) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(state == "CANCELADO") Color.Red else colorResource(R.color.esmeralda)
                    )
                ) {
                    Text(state, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    } else {
        Text("Ciclo de vida finalizado", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun MeseroOrderInfoCard(order: Order) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Mesa ${order.numeroMesa}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = colorResource(R.color.esmeralda))
                Text("Pedido #${order.orderNumber}", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            DetailItem(Icons.Default.Person, "Cliente", if(order.clienteNombre.isEmpty()) "Comensal App" else order.clienteNombre)
            if(order.clienteId.isNotEmpty()) {
                DetailItem(Icons.Default.Badge, "ID / Cédula", order.clienteId)
            }
            DetailItem(Icons.Default.Schedule, "Fecha y Hora", dateFormat.format(Date(order.fecha)))
            
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Total General", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("$${String.format("%.2f", order.total)}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = colorResource(R.color.esmeralda))
            }
        }
    }
}

@Composable
fun DetailItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(icon, null, modifier = Modifier.size(18.dp), tint = colorResource(R.color.esmeralda).copy(alpha = 0.7f))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
    }
}
