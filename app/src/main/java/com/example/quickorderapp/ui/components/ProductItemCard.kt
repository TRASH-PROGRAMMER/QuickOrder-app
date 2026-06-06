package com.example.quickorderapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.quickorderapp.domain.model.Product
import com.example.quickorderapp.R

/**
 * Componente reutilizable para mostrar un producto en una tarjeta.
 */
@Composable
fun ProductCard(
    product: Product,
    isAdmin: Boolean = false,
    onEdit: (Product) -> Unit = {},
    onDelete: (Product) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.LightGray)
            ) {
                if (product.imagenUrl.isNotBlank()) {
                    AsyncImage(
                        model = product.imagenUrl,
                        contentDescription = product.nombre,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.ic_launcher_background),
                        error = painterResource(id = R.drawable.ic_launcher_background)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Sin imagen",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                if (product.descuento > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .background(
                                Color(0xFFC62828),
                                RoundedCornerShape(bottomEnd = 8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Oferta",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Botones de acción para Admin
                if (isAdmin) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(32.dp),
                            onClick = { onEdit(product) }
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = colorResource(R.color.esmeralda),
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(32.dp),
                            onClick = { onDelete(product) }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = Color.Red,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = product.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1
                )

                Text(
                    text = product.descripcion,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "$${product.precio}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    if (product.descuento > 0) {
                        Box(
                            modifier = Modifier
                                .background(
                                    Color(0xFFD32F2F),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "-${product.descuento.toInt()}%",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
