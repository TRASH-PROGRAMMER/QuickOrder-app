@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.quickorderapp.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
import java.util.*

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
            val filteredOrders = filterOrdersByPeriod(orders, selectedPeriod)
            
            val completed = filteredOrders.count { it.estado == "COMPLETADO" }
            val pending = filteredOrders.count { it.estado == "PENDIENTE" }
            val inProgress = filteredOrders.count { it.estado == "EN PREPARACIÓN" }
            val cancelled = filteredOrders.count { it.estado == "CANCELADO" }
            val totalVentas = filteredOrders.filter { it.estado == "COMPLETADO" }.sumOf { it.total }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Selector de Período
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    periods.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = periods.size),
                            onClick = { selectedPeriod = label },
                            selected = selectedPeriod == label,
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = colorResource(R.color.esmeralda),
                                activeContentColor = Color.White
                            )
                        ) {
                            Text(label, style = MaterialTheme.typography.labelSmall)
                        }
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
                        Text("Ventas del Periodo", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.labelLarge)
                        Text("$${String.format("%.2f", totalVentas)}", color = Color.White, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.TrendingUp, null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Text(" Datos actualizados en tiempo real", color = Color.White, style = MaterialTheme.typography.labelSmall)
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

                // Gráfico de Barras Interactivo Real
                BarChartCard(filteredOrders, selectedPeriod)
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun BarChartCard(orders: List<Order>, period: String) {
    val dataPoints = remember(orders, period) {
        getChartDataPoints(orders, period)
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth().height(350.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Distribución de Pedidos", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(24.dp))
            
            if (dataPoints.isEmpty() || dataPoints.all { it.second == 0f }) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay pedidos registrados en este periodo para graficar", color = Color.Gray, fontSize = 12.sp)
                }
            } else {
                val barColor = colorResource(R.color.esmeralda)
                val backgroundColor = colorResource(R.color.esmeralda).copy(alpha = 0.05f)
                
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val width = size.width
                            val height = size.height
                            val maxVal = dataPoints.maxOf { it.second }.coerceAtLeast(1f)
                            
                            val barWidth = (width / dataPoints.size) * 0.6f
                            val spacing = (width / dataPoints.size) * 0.4f
                            
                            dataPoints.forEachIndexed { index, point ->
                                val barHeight = (point.second / maxVal) * height
                                val x = index * (barWidth + spacing) + (spacing / 2)
                                val y = height - barHeight
                                
                                // Draw background column
                                drawRect(
                                    color = backgroundColor,
                                    topLeft = Offset(x, 0f),
                                    size = Size(barWidth, height)
                                )
                                
                                // Draw actual bar
                                if (barHeight > 0) {
                                    drawRoundRect(
                                        color = barColor,
                                        topLeft = Offset(x, y),
                                        size = Size(barWidth, barHeight),
                                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx())
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(Modifier.height(8.dp))
                    
                    // Labels row
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        dataPoints.forEach { point ->
                            Text(
                                text = point.first,
                                fontSize = 9.sp,
                                color = Color.Gray,
                                modifier = Modifier.width(IntrinsicSize.Min),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

fun filterOrdersByPeriod(orders: List<Order>, period: String): List<Order> {
    val calendar = Calendar.getInstance()
    
    return when (period) {
        "Hoy" -> {
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            orders.filter { it.fecha >= calendar.timeInMillis }
        }
        "Ayer" -> {
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            val start = calendar.timeInMillis
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            val end = calendar.timeInMillis
            orders.filter { it.fecha in start..end }
        }
        "7 días" -> {
            calendar.add(Calendar.DAY_OF_YEAR, -7)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            orders.filter { it.fecha >= calendar.timeInMillis }
        }
        "Este Mes" -> {
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            orders.filter { it.fecha >= calendar.timeInMillis }
        }
        else -> orders
    }
}

fun getChartDataPoints(orders: List<Order>, period: String): List<Pair<String, Float>> {
    return when (period) {
        "Hoy", "Ayer" -> {
            // 8 bloques de 3 horas: 0-3, 3-6, ..., 21-24
            (0..7).map { i ->
                val hourStart = i * 3
                val hourEnd = hourStart + 2
                val count = orders.count {
                    val cal = Calendar.getInstance().apply { timeInMillis = it.fecha }
                    cal.get(Calendar.HOUR_OF_DAY) in hourStart..hourEnd
                }
                "${hourStart}h" to count.toFloat()
            }
        }
        "7 días" -> {
            val days = listOf("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom")
            val calendar = Calendar.getInstance()
            // Obtener los últimos 7 días terminando en hoy
            (0..6).map { i ->
                val cal = Calendar.getInstance()
                cal.add(Calendar.DAY_OF_YEAR, - (6 - i))
                val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
                val dayLabel = when(dayOfWeek) {
                    Calendar.MONDAY -> "Lun"
                    Calendar.TUESDAY -> "Mar"
                    Calendar.WEDNESDAY -> "Mie"
                    Calendar.THURSDAY -> "Jue"
                    Calendar.FRIDAY -> "Vie"
                    Calendar.SATURDAY -> "Sab"
                    else -> "Dom"
                }
                val count = orders.count {
                    val orderCal = Calendar.getInstance().apply { timeInMillis = it.fecha }
                    orderCal.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR)
                }
                dayLabel to count.toFloat()
            }
        }
        "Este Mes" -> {
            // Dividir en 4 semanas
            (1..4).map { week ->
                val count = orders.count {
                    val cal = Calendar.getInstance().apply { timeInMillis = it.fecha }
                    val weekOfMonth = cal.get(Calendar.WEEK_OF_MONTH)
                    weekOfMonth == week
                }
                "Sem $week" to count.toFloat()
            }
        }
        else -> emptyList()
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
