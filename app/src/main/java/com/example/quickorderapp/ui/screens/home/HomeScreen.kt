package com.example.quickorderapp.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quickorderapp.R
import com.example.quickorderapp.ui.components.CategoryFilterRow
import com.example.quickorderapp.ui.components.ProductCard
import com.example.quickorderapp.viewmodel.HomeViewModel
import com.example.quickorderapp.viewmodel.ProductUiState
import com.example.quickorderapp.viewmodel.ProductViewModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeScreen(
    navController: NavController,
    productViewModel: ProductViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    var selectedCategory by remember { mutableStateOf("Entradas") }
    val categories = listOf("Entradas", "Platos Fuertes", "Promociones", "Bebidas", "Postres")
    val uiState by productViewModel.uiState.collectAsState()
    val userRole by homeViewModel.userRole.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "MENU",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
        )
        
        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp),
            thickness = 2.dp,
            color = colorResource(id = R.color.gray)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        CategoryFilterRow(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it }
        )
        
        // Solo el ADMINISTRADOR puede ver el botón de agregar productos
        if (userRole == "ADMIN") {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("AddProductScreen")
                    },
                    containerColor = colorResource(id = R.color.esmeralda)
                ) {
                    Text(
                        text = "+",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        when (val state = uiState) {
            is ProductUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ProductUiState.Success -> {
                val filteredProducts = state.products.filter { it.categoria == selectedCategory }
                
                if (filteredProducts.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No hay productos en $selectedCategory")
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredProducts) { product ->
                            ProductCard(
                                product = product,
                                isAdmin = userRole == "ADMIN",
                                onDelete = { productViewModel.deleteProduct(it) },
                                onEdit = {
                                    navController.navigate("AddProductScreen?productId=${it.id}")
                                }
                            )
                        }
                    }
                }
            }
            is ProductUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
