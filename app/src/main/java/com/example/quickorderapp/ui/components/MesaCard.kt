package com.example.quickorderapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.TableBar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickorderapp.R
import com.example.quickorderapp.domain.model.Mesa

@Composable
fun MesaCard(
    mesa: Mesa,
    isAdmin: Boolean = false,
    onEdit: (Mesa) -> Unit = {},
    onDelete: (Mesa) -> Unit = {},
    onClick: (Mesa) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = { onClick(mesa) }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = when (mesa.estado) {
                        "Libre" -> colorResource(R.color.esmeralda).copy(alpha = 0.2f)
                        "Ocupada" -> Color.Red.copy(alpha = 0.2f)
                        else -> Color.Gray.copy(alpha = 0.2f)
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.TableBar,
                            contentDescription = null,
                            tint = when (mesa.estado) {
                                "Libre" -> colorResource(R.color.esmeralda)
                                "Ocupada" -> Color.Red
                                else -> Color.Gray
                            }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(
                        text = "Mesa ${mesa.numero}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Capacidad: ${mesa.capacidad} pers.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = mesa.estado,
                        style = MaterialTheme.typography.labelSmall,
                        color = when (mesa.estado) {
                            "Libre" -> colorResource(R.color.esmeralda)
                            "Ocupada" -> Color.Red
                            else -> Color.Gray
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (isAdmin) {
                Row {
                    IconButton(onClick = { onEdit(mesa) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = colorResource(R.color.esmeralda))
                    }
                    IconButton(onClick = { onDelete(mesa) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                    }
                }
            }
        }
    }
}
