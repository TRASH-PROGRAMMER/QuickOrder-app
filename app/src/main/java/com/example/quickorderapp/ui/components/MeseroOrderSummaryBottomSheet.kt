package com.example.quickorderapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.quickorderapp.R
import com.example.quickorderapp.domain.model.Mesa
import com.example.quickorderapp.viewmodel.CartState
import androidx.compose.foundation.layout.FlowRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeseroOrderSummaryBottomSheet(
    cartState: CartState,
    mesas: List<Mesa>,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int, String) -> Unit,
    isLoading: Boolean
) {
    var clienteNombre by remember { mutableStateOf("") }
    var clienteId by remember { mutableStateOf("") }
    var selectedMesa by remember { mutableStateOf<Int?>(null) }
    var notas by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Resumen del Pedido (Manual)", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, null) }
            }

            // Product List
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                cartState.items.forEach { item ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${item.cantidad}x ${item.productoNombre}", modifier = Modifier.weight(1f))
                        Text("$${String.format("%.2f", item.subtotal)}", fontWeight = FontWeight.Bold, color = colorResource(R.color.esmeralda))
                    }
                }
            }

            HorizontalDivider()

            // Mandatory Fields
            Text("Datos del Cliente", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = clienteNombre,
                onValueChange = { clienteNombre = it },
                label = { Text("Nombre Completo *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Person, null) }
            )
            OutlinedTextField(
                value = clienteId,
                onValueChange = { clienteId = it },
                label = { Text("Número de Cédula *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Badge, null) }
            )

            Text("Seleccionar Mesa *", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                mesas.forEach { mesa ->
                    FilterChip(
                        selected = selectedMesa == mesa.numero,
                        onClick = { selectedMesa = mesa.numero },
                        label = { Text("Mesa ${mesa.numero}") },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            OutlinedTextField(
                value = notas,
                onValueChange = { notas = it },
                label = { Text("Notas del pedido") },
                placeholder = { Text("Ej: Sin cebolla, poco picante...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.AutoMirrored.Filled.Notes, null) }
            )

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
                    onClick = { selectedMesa?.let { onConfirm(clienteNombre, clienteId, it, notas) } },
                    enabled = clienteNombre.isNotBlank() && clienteId.isNotBlank() && selectedMesa != null && !isLoading,
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
