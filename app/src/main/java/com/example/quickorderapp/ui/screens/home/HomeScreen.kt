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

import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ShoppingCart
import com.example.quickorderapp.viewmodel.CartState
import com.example.quickorderapp.viewmodel.OrderViewModel
import com.example.quickorderapp.viewmodel.OrderActionStatus
import com.example.quickorderapp.ui.components.OrderSummaryBottomSheet
import com.example.quickorderapp.ui.components.MeseroOrderSummaryBottomSheet

import androidx.compose.material.icons.filled.Menu
import com.example.quickorderapp.domain.model.Mesa

import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    productViewModel: ProductViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel(),
    meseroViewModel: com.example.quickorderapp.viewmodel.MeseroViewModel = hiltViewModel(),
    onOpenDrawer: () -> Unit = {}
) {
    val uiState by productViewModel.uiState.collectAsStateWithLifecycle()
    val userRole by homeViewModel.userRole.collectAsStateWithLifecycle()
    val categories by productViewModel.categories.collectAsStateWithLifecycle()
    val cartState by orderViewModel.cartState.collectAsStateWithLifecycle()
    val manualCart by meseroViewModel.manualCart.collectAsStateWithLifecycle()
    val orderStatus by orderViewModel.orderActionStatus.collectAsStateWithLifecycle()
    val meseroOrderStatus by meseroViewModel.orderActionStatus.collectAsStateWithLifecycle()
    val mesas by orderViewModel.mesas.collectAsStateWithLifecycle()

    var showSummary by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }
    var selectedProductId by remember { mutableStateOf<Int?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(orderStatus, meseroOrderStatus) {
        val currentStatus = if (userRole == "MESERO") meseroOrderStatus else orderStatus
        
        when (currentStatus) {
            is OrderActionStatus.Success -> {
                showSummary = false
                showConfirmation = true
            }
            is OrderActionStatus.Error -> {
                snackbarHostState.showSnackbar(currentStatus.message)
                if (userRole == "MESERO") meseroViewModel.resetActionStatus()
                else orderViewModel.resetActionStatus()
            }
            else -> {}
        }
    }

    val effectiveCart = if (userRole == "MESERO") manualCart else cartState

    HomeScreenContent(
        uiState = uiState,
        userRole = userRole,
        categories = categories.map { it.nombre },
        cartState = effectiveCart,
        selectedProductId = selectedProductId,
        snackbarHostState = snackbarHostState,
        onOpenDrawer = onOpenDrawer,
        onAddProduct = { navController.navigate("AddProductScreen") },
        onEditProduct = { navController.navigate("AddProductScreen?productId=${it.id}") },
        onDeleteProduct = { productViewModel.deleteProduct(it) },
        onManageTables = { navController.navigate("MesaScreen") },
        onSetSelectedProduct = { selectedProductId = it.id },
        onSetQuantity = { qty ->
            val pid = selectedProductId
            if (pid != null) {
                val product = (uiState as? ProductUiState.Success)?.products?.find { it.id == pid }
                product?.let {
                    if (userRole == "MESERO") {
                        meseroViewModel.setManualQuantity(it, qty)
                    } else {
                        orderViewModel.setProductQuantity(it, qty)
                    }
                }
            }
        },
        onViewCart = { 
            showSummary = true
        },
        onLogout = {
            homeViewModel.logout {
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    )

    if (showSummary) {
        if (userRole == "COMENSAL") {
            OrderSummaryBottomSheet(
                cartState = cartState,
                mesas = mesas,
                onDismiss = { showSummary = false },
                onSetMesa = { mesaNum ->
                    val mesa = mesas.find { it.numero == mesaNum }
                    if (mesa?.estado == "Libre") {
                        orderViewModel.setMesa(mesaNum)
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Esa mesa ya tiene un pedido activo o está reservada.")
                        }
                    }
                },
                onSetNotas = { orderViewModel.setNotas(it) },
                onConfirm = { 
                    if (cartState.mesaSeleccionada != null) {
                        orderViewModel.confirmOrder()
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Debes seleccionar una mesa")
                        }
                    }
                },
                isLoading = orderStatus is OrderActionStatus.Loading
            )
        } else if (userRole == "MESERO") {
            MeseroOrderSummaryBottomSheet(
                cartState = manualCart,
                mesas = mesas,
                onDismiss = { showSummary = false },
                onConfirm = { nombre, cedula, mesa, notas ->
                    meseroViewModel.confirmManualOrder(nombre, cedula, mesa, notas)
                },
                isLoading = meseroOrderStatus is OrderActionStatus.Loading
            )
        }
    }

    if (showConfirmation) {
        val order = if (userRole == "MESERO") {
            (meseroOrderStatus as? OrderActionStatus.Success)?.order
        } else {
            (orderStatus as? OrderActionStatus.Success)?.order
        }
        
        AlertDialog(
            onDismissRequest = { 
                showConfirmation = false
                if (userRole == "MESERO") meseroViewModel.resetActionStatus()
                else orderViewModel.resetActionStatus()
            },
            title = { 
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, null, tint = colorResource(R.color.esmeralda))
                    Spacer(Modifier.width(8.dp))
                    Text("Pedido confirmado") 
                }
            },
            text = {
                Column {
                    Text(if (userRole == "MESERO") "¡Pedido manual registrado!" else "¡Pedido confirmado con éxito!", fontWeight = FontWeight.Bold)
                    Text("El pedido ha sido guardado y ya está visible en el sistema.")
                    Spacer(modifier = Modifier.height(12.dp))
                    order?.let {
                        Text("Número de pedido: #${it.orderNumber}", style = MaterialTheme.typography.bodySmall)
                        Text("Mesa: ${it.numeroMesa}")
                        Text("Total: $${String.format("%.2f", it.total)}", fontWeight = FontWeight.Bold, color = colorResource(R.color.esmeralda))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { 
                        showConfirmation = false
                        if (userRole == "MESERO") meseroViewModel.resetActionStatus()
                        else orderViewModel.resetActionStatus()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.esmeralda))
                ) { Text("Aceptar") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeScreenContent(
    uiState: ProductUiState,
    userRole: String,
    categories: List<String>,
    cartState: CartState,
    selectedProductId: Int?,
    snackbarHostState: SnackbarHostState,
    onOpenDrawer: () -> Unit,
    onAddProduct: () -> Unit,
    onEditProduct: (Product) -> Unit,
    onDeleteProduct: (Product) -> Unit,
    onManageTables: () -> Unit,
    onSetSelectedProduct: (Product) -> Unit,
    onSetQuantity: (Int) -> Unit,
    onViewCart: () -> Unit,
    onLogout: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("") }
    
    LaunchedEffect(categories) {
        if (selectedCategory.isEmpty() && categories.isNotEmpty()) {
            selectedCategory = categories.first()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "MENU", 
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    ) 
                },
                navigationIcon = {
                    if (userRole == "COMENSAL" || userRole == "MESERO") {
                        IconButton(onClick = onOpenDrawer) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                },
                actions = {
                    if (userRole == "ADMIN") {
                        IconButton(onClick = onManageTables) {
                            Icon(Icons.Default.TableBar, contentDescription = "Gestionar Mesas", tint = colorResource(R.color.esmeralda))
                        }
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar Sesión", tint = MaterialTheme.colorScheme.error)
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
        Row(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Main Content Area (Products Grid)
            Column(
                modifier = Modifier
                    .weight(1f)
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
                            CircularProgressIndicator(color = colorResource(R.color.esmeralda))
                        }
                    }
                    is ProductUiState.Success -> {
                        val filteredProducts = uiState.products
                            .filter { it.categoria == selectedCategory }
                            .filter { if (userRole == "ADMIN") true else it.disponible }
                        
                        if (filteredProducts.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("No hay productos en $selectedCategory")
                            }
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 100.dp)
                            ) {
                                items(filteredProducts) { product ->
                                    val qty = cartState.items.find { it.productoId == product.id }?.cantidad ?: 0
                                    ProductCard(
                                        product = product,
                                        isAdmin = userRole == "ADMIN",
                                        isSelected = selectedProductId == product.id,
                                        quantityInCart = qty,
                                        onDelete = onDeleteProduct,
                                        onEdit = onEditProduct,
                                        onClick = onSetSelectedProduct
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

            // Global POS Vertical Selector
            if (userRole == "COMENSAL" || userRole == "MESERO") {
                Column(
                    modifier = Modifier
                        .width(60.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    (0..9).forEach { num ->
                        val currentQty = cartState.items.find { it.productoId == selectedProductId }?.cantidad ?: 0
                        val isSelectedQty = currentQty == num && selectedProductId != null
                        
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelectedQty) colorResource(R.color.esmeralda) else Color.White)
                                .border(1.dp, colorResource(R.color.esmeralda).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                .clickable { onSetQuantity(num) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = num.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelectedQty) Color.White else colorResource(R.color.esmeralda)
                            )
                        }
                    }
                }
            }
        }

        // Floating Summary Button for COMENSAL or MESERO
        if ((userRole == "COMENSAL" || userRole == "MESERO") && cartState.items.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(bottom = 16.dp, start = 16.dp, end = 80.dp), contentAlignment = Alignment.BottomCenter) {
                Button(
                    onClick = onViewCart,
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.esmeralda)),
                    elevation = ButtonDefaults.buttonElevation(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("${cartState.items.sumOf { it.cantidad }} platos seleccionados", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.8f))
                            Text(if (userRole == "MESERO") "Registro Manual" else "Ver Resumen", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        Text("$${String.format("%.2f", cartState.total)}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}

