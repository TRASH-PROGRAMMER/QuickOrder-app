package com.example.quickorderapp.ui.screens.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quickorderapp.domain.model.Product
import com.example.quickorderapp.viewmodel.ProductUiState
import com.example.quickorderapp.viewmodel.ProductViewModel

/**
 * Pantalla que muestra el catálogo de productos.
 * Implementa el manejo de estados (Cargando, Éxito, Error) según reglas.md.
 */
@Composable
fun ProductScreen(viewModel: ProductViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Productos", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.weight(1f)) {
            when (val state = uiState) {
                is ProductUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ProductUiState.Success -> {
                    ProductList(products = state.products)
                }
                is ProductUiState.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        Button(
            onClick = {
                viewModel.addProduct(
                    Product(
                        nombre = "Nuevo Producto",
                        precio = 10.0,
                        descripcion = "Descripción del nuevo producto",
                        categoria = "General"
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "Agregar Producto de Prueba")
        }
    }
}

@Composable
fun ProductList(products: List<Product>) {
    if (products.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No hay productos disponibles")
        }
    } else {
        LazyColumn {
            items(products) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = product.nombre)
                        Text(text = "$${product.precio}")
                    }
                }
            }
        }
    }
}
