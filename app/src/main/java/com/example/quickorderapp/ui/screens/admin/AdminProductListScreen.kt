@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.quickorderapp.ui.screens.admin

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.quickorderapp.R
import com.example.quickorderapp.domain.model.Product
import com.example.quickorderapp.viewmodel.ProductUiState
import com.example.quickorderapp.viewmodel.ProductViewModel

@Composable
fun AdminProductListScreen(
    navController: NavController,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    var expandedCategories by remember { mutableStateOf(setOf<String>()) }
    var showDeleteDialog by remember { mutableStateOf<Product?>(null) }
    var sortOrder by remember { mutableStateOf("Nombre A-Z") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Productos", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { navController.navigate("AddProductScreen") }) {
                        Icon(Icons.Default.Add, contentDescription = "Añadir")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Search and Filter Bar
            ProductSearchBar(searchQuery) { searchQuery = it }
            
            if (uiState is ProductUiState.Success) {
                val allProducts = (uiState as ProductUiState.Success).products
                val categories = allProducts.map { it.categoria }.distinct().sorted()
                
                // Quick Stats
                QuickProductStats(allProducts, categories.size)

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    categories.forEach { category ->
                        val productsInCategory = allProducts
                            .filter { it.categoria == category }
                            .filter { it.nombre.contains(searchQuery, ignoreCase = true) || it.descripcion.contains(searchQuery, ignoreCase = true) }
                        
                        if (productsInCategory.isNotEmpty()) {
                            item {
                                CategoryHeader(
                                    category = category,
                                    count = productsInCategory.size,
                                    isExpanded = expandedCategories.contains(category),
                                    onToggle = {
                                        expandedCategories = if (expandedCategories.contains(category)) {
                                            expandedCategories - category
                                        } else {
                                            expandedCategories + category
                                        }
                                    }
                                )
                            }

                            if (expandedCategories.contains(category)) {
                                items(productsInCategory) { product ->
                                    AdminProductCard(
                                        product = product,
                                        onEdit = { navController.navigate("AddProductScreen?productId=${product.id}") },
                                        onDelete = { showDeleteDialog = product },
                                        onToggleAvailable = { viewModel.toggleAvailability(product) }
                                    )
                                }
                            }
                        }
                    }
                }
            } else if (uiState is ProductUiState.Loading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colorResource(R.color.esmeralda))
                }
            }
        }
    }

    showDeleteDialog?.let { product ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Eliminar Producto") },
            text = { Text("¿Está seguro de eliminar '${product.nombre}'? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteProduct(product)
                        showDeleteDialog = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun ProductSearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = { Text("Buscar por nombre o descripción...") },
        leadingIcon = { Icon(Icons.Default.Search, null) },
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(R.color.esmeralda)
        )
    )
}

@Composable
fun QuickProductStats(products: List<Product>, categoryCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCardSmall("Total", products.size.toString(), Icons.Default.Inventory, Modifier.weight(1f))
        StatCardSmall("Activos", products.count { it.disponible }.toString(), Icons.Default.CheckCircle, Modifier.weight(1f))
        StatCardSmall("Ocultos", products.count { !it.disponible }.toString(), Icons.Default.VisibilityOff, Modifier.weight(1f))
    }
}

@Composable
fun StatCardSmall(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, modifier = Modifier.size(16.dp), tint = colorResource(R.color.esmeralda))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(title, fontSize = 10.sp, color = Color.Gray)
        }
    }
}

@Composable
fun CategoryHeader(category: String, count: Int, isExpanded: Boolean, onToggle: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onToggle() },
        color = colorResource(R.color.esmeralda).copy(alpha = 0.05f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                null,
                tint = colorResource(R.color.esmeralda)
            )
            Spacer(Modifier.width(12.dp))
            Text(category, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.weight(1f))
            Badge(containerColor = colorResource(R.color.esmeralda)) {
                Text("$count", color = Color.White)
            }
        }
    }
}

@Composable
fun AdminProductCard(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleAvailable: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (product.disponible) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp).height(IntrinsicSize.Min)) {
            Box {
                AsyncImage(
                    model = product.imagenUrl,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                if (!product.disponible) {
                    Box(Modifier.matchParentSize().background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.VisibilityOff, null, tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                }
            }
            
            Spacer(Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(product.nombre, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(product.descripcion, fontSize = 11.sp, color = Color.Gray, maxLines = 2)
                Spacer(Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("$${String.format("%.2f", product.precio)}", fontWeight = FontWeight.ExtraBold, color = colorResource(R.color.esmeralda))
                    if (product.esPromocion) {
                        Spacer(Modifier.width(8.dp))
                        Text("-${product.descuento.toInt()}%", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Edit, null, tint = colorResource(R.color.esmeralda))
                }
                IconButton(onClick = onToggleAvailable, modifier = Modifier.size(32.dp)) {
                    Icon(if (product.disponible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, tint = Color.Gray)
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, null, tint = Color.Red)
                }
            }
        }
    }
}
