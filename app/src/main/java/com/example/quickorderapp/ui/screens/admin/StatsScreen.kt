@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.quickorderapp.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quickorderapp.R
import com.example.quickorderapp.domain.model.Order
import com.example.quickorderapp.viewmodel.MeseroViewModel

@Composable
fun StatsScreen(
    viewModel: MeseroViewModel = hiltViewModel()
) {
    val uiState by viewModel.allOrders.collectAsStateWithLifecycle()
    var selectedPeriod by remember { mutableStateOf("Hoy") }
    val periods = listOf("Hoy", "Ayer", "7 días", "Este Mes")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Estadísticas", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = colorResource(R.color.esmeralda))
            }
        } else if (uiState.orders.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.BarChart, null, modifier = Modifier.size(64.dp), tint = Color.Gray.copy(alpha = 0.5f))
                    Spacer(Modifier.height(16.dp))
                    Text("Aún no existen suficientes pedidos.", color = Color.Gray)
                }
            }
        } else {
            val orders = uiState.orders
            val completed = orders.count { it.estado == "COMPLETADO" }
            val pending = orders.count { it.estado == "PENDIENTE" }
            val inProgress = orders.count { it.estado == "EN PREPARACIÓN" }
            val cancelled = orders.count { it.estado == "CANCELADO" }
            val totalVentas = orders.filter { it.estado == "COMPLETADO" }.sumOf { it.total }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Selector de Período
                ScrollableTabRow(
                    selectedTabIndex = periods.indexOf(selectedPeriod),
                    edgePadding = 0.dp,
                    containerColor = Color.Transparent,
                    divider = {},
                    indicator = {}
                ) {
                    periods.forEach { period ->
                        Tab(
                            selected = selectedPeriod == period,
                            onClick = { selectedPeriod = period },
                            text = { 
                                Text(
                                    period, 
                                    fontWeight = if (selectedPeriod == period) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedPeriod == period) colorResource(R.color.esmeralda) else MaterialTheme.colorScheme.onSurfaceVariant
                                ) 
                            }
                        )
                    }
                }

                // Resumen de Ventas
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = colorResource(R.color.esmeralda)
                    )
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Ventas Totales (Completados)", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.labelLarge)
                        Text("$${String.format("%.2f", totalVentas)}", color = Color.White, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TrendingUp, null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Text(" +12% vs periodo anterior", color = Color.White, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }

                Text("Estado de Pedidos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatBox("Completados", completed.toString(), Icons.Default.CheckCircle, Color(0xFF4CAF50), modifier = Modifier.weight(1f))
                    StatBox("Pendientes", pending.toString(), Icons.Default.Timer, Color(0xFFFFA000), modifier = Modifier.weight(1f))
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatBox("En Cocina", inProgress.toString(), Icons.Default.Restaurant, Color(0xFF2196F3), modifier = Modifier.weight(1f))
                    StatBox("Cancelados", cancelled.toString(), Icons.Default.Cancel, Color(0xFFF44336), modifier = Modifier.weight(1f))
                }

                // Gráfico Simulado (Placeholder visual profesional)
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth().height(240.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Tendencia de Pedidos", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.weight(1f))
                        Box(modifier = Modifier.fillMaxWidth().height(140.dp).background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                            Text("Gráfico de Líneas", color = Color.Gray, style = MaterialTheme.typography.labelMedium)
                            // Aquí iría la implementación de Canvas para el gráfico de líneas
                        }
                    }
                }
                
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun StatBox(title: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(12.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
