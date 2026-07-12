package com.example.quickorderapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.TableRestaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickorderapp.R
import com.example.quickorderapp.domain.model.Mesa
import com.example.quickorderapp.viewmodel.CartState

import androidx.compose.foundation.layout.FlowRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSummaryBottomSheet(
    cartState: CartState,
    mesas: List<Mesa>,
    onDismiss: () -> Unit,
    onSetMesa: (Int) -> Unit,
    onSetNotas: (String) -> Unit,
    onConfirm: () -> Unit,
    isLoading: Boolean
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Resumen del Pedido",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .padding(vertical = 8.dp)
            ) {
                items(cartState.items) { item ->
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.productoNombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("${item.cantidad} x $${item.precioUnitario}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text(
                                text = "$${String.format("%.2f", item.subtotal)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = colorResource(R.color.esmeralda)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Notas
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Notes, null, modifier = Modifier.size(20.dp), tint = colorResource(R.color.esmeralda))
                Spacer(Modifier.width(8.dp))
                Text("Notas adicionales", fontWeight = FontWeight.Bold)
            }
            OutlinedTextField(
                value = cartState.notas,
                onValueChange = onSetNotas,
                placeholder = { Text("Ej: Sin cebolla, extra queso...", fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.esmeralda)
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Mesa
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.TableRestaurant, null, modifier = Modifier.size(20.dp), tint = colorResource(R.color.esmeralda))
                Spacer(Modifier.width(8.dp))
                Text("Selecciona tu Mesa *", fontWeight = FontWeight.Bold)
            }
            
            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                mesas.forEach { mesa ->
                    val isAvailable = mesa.estado == "Libre"
                    val color = when(mesa.estado) {
                        "Libre" -> Color(0xFF4CAF50)
                        "Ocupada" -> Color(0xFFF44336)
                        "Reservada" -> Color(0xFFFFA000)
                        else -> Color.Gray
                    }

                    FilterChip(
                        selected = cartState.mesaSeleccionada == mesa.numero,
                        onClick = { 
                            if (isAvailable) {
                                onSetMesa(mesa.numero)
                            } else {
                                // Feedback for unavailable mesa
                            }
                        },
                        label = { 
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
                                Spacer(Modifier.width(6.dp))
                                Text("Mesa ${mesa.numero}") 
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        enabled = true, // We keep it enabled to show feedback if clicked
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = colorResource(R.color.esmeralda).copy(alpha = 0.2f),
                            selectedLabelColor = colorResource(R.color.esmeralda)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Total y Confirmar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total a pagar", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                    Text("$${String.format("%.2f", cartState.total)}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = colorResource(R.color.esmeralda))
                }
                
                Button(
                    onClick = onConfirm,
                    enabled = cartState.mesaSeleccionada != null && cartState.items.isNotEmpty() && !isLoading,
                    modifier = Modifier.height(56.dp).width(160.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.esmeralda))
                ) {
                    if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    else Text("Confirmar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
