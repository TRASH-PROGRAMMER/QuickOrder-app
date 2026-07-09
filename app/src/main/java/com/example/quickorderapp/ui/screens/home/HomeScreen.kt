package com.example.quickorderapp.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.TableBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.quickorderapp.R
import com.example.quickorderapp.domain.model.Product
import com.example.quickorderapp.ui.components.CategoryFilterRow
import com.example.quickorderapp.ui.components.ProductCard
import com.example.quickorderapp.viewmodel.HomeViewModel
import com.example.quickorderapp.viewmodel.ProductUiState
import com.example.quickorderapp.viewmodel.ProductViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    productViewModel: ProductViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by productViewModel.uiState.collectAsStateWithLifecycle()
    val userRole by homeViewModel.userRole.collectAsStateWithLifecycle()

    HomeScreenContent(
        uiState = uiState,
        userRole = userRole,
        onAddProduct = { navController.navigate("AddProductScreen") },
        onEditProduct = { navController.navigate("AddProductScreen?productId=${it.id}") },
        onDeleteProduct = { productViewModel.deleteProduct(it) },
        onManageTables = { navController.navigate("MesaScreen") },
        onLogout = {
            homeViewModel.logout {
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeScreenContent(
    uiState: ProductUiState,
    userRole: String,
    onAddProduct: () -> Unit,
    onEditProduct: (Product) -> Unit,
    onDeleteProduct: (Product) -> Unit,
    onManageTables: () -> Unit,
    onLogout: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("Entradas") }
    val categories = listOf("Entradas", "Platos Fuertes", "Promociones", "Bebidas", "Postres")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "MENU", 
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    ) 
                },
                actions = {
                    if (userRole == "ADMIN") {
                        IconButton(onClick = onManageTables) {
                            Icon(
                                imageVector = Icons.Default.TableBar,
                                contentDescription = "Gestionar Mesas",
                                tint = colorResource(R.color.esmeralda)
                            )
                        }
                    }
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Cerrar Sesión",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (userRole == "ADMIN") {
                FloatingActionButton(
                    onClick = onAddProduct,
                    containerColor = colorResource(id = R.color.esmeralda)
                ) {
                    Text("+", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            CategoryFilterRow(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it },
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            when (uiState) {
                is ProductUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ProductUiState.Success -> {
                    val filteredProducts = uiState.products.filter { it.categoria == selectedCategory }
                    
                    if (filteredProducts.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = "No hay productos en $selectedCategory",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 150.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(filteredProducts) { product ->
                                ProductCard(
                                    product = product,
                                    isAdmin = userRole == "ADMIN",
                                    onDelete = onDeleteProduct,
                                    onEdit = onEditProduct
                                )
                            }
                        }
                    }
                }
                is ProductUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
