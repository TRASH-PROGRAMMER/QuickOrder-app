@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.quickorderapp.ui.screens.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.quickorderapp.viewmodel.OrderViewModel
import java.text.SimpleDateFormat
import java.util.*

import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.filled.*

@Composable
fun OrderDetailScreen(
    orderId: Int,
    navController: NavController,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val order by viewModel.getOrderById(orderId).collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Pedido") },
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
                OrderInfoCard(o)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Productos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(o.items) { item ->
                        ListItem(
                            headlineContent = { Text(item.productoNombre) },
                            supportingContent = { Text("${item.cantidad} x $${item.precioUnitario}") },
                            trailingContent = { Text("$${item.subtotal}", fontWeight = FontWeight.Bold) }
                        )
                    }
                }
                if (o.notas.isNotBlank()) {
                    Text("Notas:", fontWeight = FontWeight.Bold)
                    Text(o.notas, style = MaterialTheme.typography.bodyMedium)
                }
                
                if (o.estado == "PENDIENTE") {
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { 
                                viewModel.editOrder(o)
                                navController.navigate("inicio") // Go back to menu to edit
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.esmeralda))
                        ) {
                            Text("Editar Pedido")
                        }
                        OutlinedButton(
                            onClick = { viewModel.cancelOrder(o.id, o.remoteId) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                        ) {
                            Text("Cancelar")
                        }
                    }
                }
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun OrderInfoCard(order: Order) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Pedido #${order.orderNumber}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = colorResource(R.color.esmeralda))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = colorResource(R.color.esmeralda).copy(alpha = 0.1f)
                ) {
                    Text(order.estado, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), color = colorResource(R.color.esmeralda), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if(order.clienteNombre.isNotEmpty()) {
                DetailItem(Icons.Default.Person, "Nombre del Cliente", order.clienteNombre)
            }
            if(order.clienteId.isNotEmpty()) {
                DetailItem(Icons.Default.Badge, "ID / Cédula", order.clienteId)
            }
            DetailItem(Icons.Default.TableBar, "Mesa Seleccionada", "Mesa ${order.numeroMesa}")
            DetailItem(Icons.Default.Schedule, "Realizado el", dateFormat.format(Date(order.fecha)))
            
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Monto Total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
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
