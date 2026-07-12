package com.example.quickorderapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.quickorderapp.domain.model.Product
import com.example.quickorderapp.R

import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove

@Composable
fun ProductCard(
    product: Product,
    isAdmin: Boolean = false,
    isSelected: Boolean = false,
    quantityInCart: Int = 0,
    onEdit: (Product) -> Unit = {},
    onDelete: (Product) -> Unit = {},
    onClick: (Product) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val placeholderPainter = rememberVectorPainter(Icons.Outlined.Restaurant)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        border = if (isSelected) BorderStroke(2.dp, colorResource(R.color.esmeralda)) else null,
        elevation = CardDefaults.cardElevation(if (isSelected) 8.dp else 2.dp),
        onClick = { if (!isAdmin) onClick(product) }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.LightGray.copy(alpha = 0.5f))
            ) {
                if (product.imagenUrl.isNotBlank()) {
                    AsyncImage(
                        model = product.imagenUrl,
                        contentDescription = product.nombre,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = placeholderPainter,
                        error = placeholderPainter
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Outlined.Restaurant, contentDescription = null, modifier = Modifier.size(32.dp), tint = Color.Gray)
                    }
                }

                // Badge de Cantidad
                if (quantityInCart > 0) {
                    Surface(
                        modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
                        shape = CircleShape,
                        color = colorResource(R.color.esmeralda),
                        shadowElevation = 4.dp
                    ) {
                        Text(
                            text = "x$quantityInCart",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (product.descuento > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .background(Color(0xFFC62828), RoundedCornerShape(bottomEnd = 8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Oferta", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                if (isAdmin) {
                    Row(
                        modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.9f), modifier = Modifier.size(28.dp), onClick = { onEdit(product) }) {
                            Icon(Icons.Default.Edit, "Editar", tint = colorResource(R.color.esmeralda), modifier = Modifier.padding(6.dp))
                        }
                        Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.9f), modifier = Modifier.size(28.dp), onClick = { onDelete(product) }) {
                            Icon(Icons.Default.Delete, "Eliminar", tint = Color.Red, modifier = Modifier.padding(6.dp))
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = product.nombre, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
                Text(text = product.descripcion, fontSize = 10.sp, color = Color.Gray, maxLines = 1)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Column {
                        if (product.descuento > 0) {
                            Text(
                                text = "$${product.precio}",
                                style = MaterialTheme.typography.labelSmall.copy(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough),
                                color = Color.Gray
                            )
                            val discountedPrice = product.precio * (1 - (product.descuento / 100))
                            Text(
                                text = "$${String.format("%.2f", discountedPrice)}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = colorResource(R.color.esmeralda)
                            )
                        } else {
                            Text(
                                text = "$${product.precio}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = colorResource(R.color.esmeralda)
                            )
                        }
                    }
                    if (isSelected) {
                        Icon(Icons.Default.CheckCircle, null, tint = colorResource(R.color.esmeralda), modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

