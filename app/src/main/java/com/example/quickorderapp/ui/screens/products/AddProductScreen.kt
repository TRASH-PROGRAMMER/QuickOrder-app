package com.example.quickorderapp.ui.screens.products

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.quickorderapp.R
import com.example.quickorderapp.domain.model.Product
import com.example.quickorderapp.viewmodel.ProductUiState
import com.example.quickorderapp.viewmodel.ProductViewModel

@Composable
fun AddProductScreen(
    navController: NavController,
    productId: Int = -1,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AddProductScreenContent(
        productId = productId,
        uiState = uiState,
        onBack = { navController.popBackStack() },
        onSave = { product ->
            if (productId == -1) viewModel.addProduct(product)
            else viewModel.updateProduct(product)
            navController.popBackStack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreenContent(
    productId: Int,
    uiState: ProductUiState,
    onBack: () -> Unit,
    onSave: (Product) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Entradas") }
    var descuento by remember { mutableStateOf("0") }
    var esPromocion by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> uri?.let { imagenUrl = it.toString() } }
    )

    LaunchedEffect(uiState) {
        if (productId != -1 && uiState is ProductUiState.Success) {
            uiState.products.find { it.id == productId }?.let {
                nombre = it.nombre
                precio = it.precio.toString()
                descripcion = it.descripcion
                imagenUrl = it.imagenUrl
                categoria = it.categoria
                descuento = it.descuento.toString()
                esPromocion = it.esPromocion
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (productId == -1) "Nuevo Producto" else "Editar Producto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
                    .border(1.dp, Color.Green, RoundedCornerShape(12.dp))
                    .clickable { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                contentAlignment = Alignment.Center
            ) {
                if (imagenUrl.isNotBlank()) AsyncImage(model = imagenUrl, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                else Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.Gray)
                    Text("Toca para añadir foto", color = Color.Gray)
                }
            }

            OutlinedTextField(
                value = nombre, 
                onValueChange = { nombre = it }, 
                label = { Text("Nombre del producto") }, 
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.esmeralda),
                    unfocusedBorderColor = colorResource(R.color.esmeralda)
                )
            )
            
            OutlinedTextField(
                value = precio, 
                onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) precio = it }, 
                label = { Text("Precio") }, 
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), 
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.esmeralda),
                    unfocusedBorderColor = colorResource(R.color.esmeralda)
                )
            )

            OutlinedTextField(
                value = descripcion, 
                onValueChange = { descripcion = it }, 
                label = { Text("Descripción") }, 
                modifier = Modifier.fillMaxWidth(), 
                minLines = 3,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.esmeralda),
                    unfocusedBorderColor = colorResource(R.color.esmeralda)
                )
            )

            Text("Categoría", style = MaterialTheme.typography.labelLarge)
            Row(Modifier.horizontalScroll(rememberScrollState())) {
                listOf("Entradas", "Platos Fuertes", "Bebidas", "Postres").forEach { cat ->
                    FilterChip(selected = categoria == cat, onClick = { categoria = cat }, label = { Text(cat) }, modifier = Modifier.padding(end = 4.dp))
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = esPromocion, onCheckedChange = { esPromocion = it })
                Text("¿Está en Promoción?")
            }

            if (esPromocion) {
                OutlinedTextField(
                    value = descuento, 
                    onValueChange = { if (it.all { c -> c.isDigit() }) descuento = it }, 
                    label = { Text("% Descuento") }, 
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), 
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(R.color.esmeralda),
                        unfocusedBorderColor = colorResource(R.color.esmeralda)
                    )
                )
            }

            Button(
                enabled = nombre.isNotBlank() && precio.isNotBlank(),
                onClick = {
                    onSave(Product(id = if (productId == -1) 0 else productId, nombre = nombre, precio = precio.toDoubleOrNull() ?: 0.0, descripcion = descripcion, imagenUrl = imagenUrl, categoria = categoria, descuento = if (esPromocion) (descuento.toDoubleOrNull() ?: 0.0) else 0.0, esPromocion = esPromocion))
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.esmeralda)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (productId == -1) "Guardar Producto" else "Actualizar Producto")
            }
        }
    }
}
