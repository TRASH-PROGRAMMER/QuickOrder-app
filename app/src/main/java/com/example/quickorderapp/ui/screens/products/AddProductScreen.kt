@file:OptIn(ExperimentalMaterial3Api::class)
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.quickorderapp.viewmodel.SaveStatus
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun AddProductScreen(
    navController: NavController,
    productId: Int = -1,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val saveStatus by viewModel.saveStatus.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(saveStatus) {
        if (saveStatus is SaveStatus.Success) {
            scope.launch {
                snackbarHostState.showSnackbar("Producto guardado correctamente")
            }
            // Esperamos un momento para que el usuario vea el icono de check antes de volver
            kotlinx.coroutines.delay(1000)
            viewModel.resetSaveStatus()
            navController.popBackStack()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (productId == -1) "Nuevo Producto" else "Editar Producto", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        AddProductScreenContent(
            modifier = Modifier.padding(padding),
            productId = productId,
            uiState = uiState,
            saveStatus = saveStatus,
            categories = categories.map { it.nombre },
            onSaveProduct = { product ->
                if (productId == -1) viewModel.addProduct(product)
                else viewModel.updateProduct(product)
            }
        )
    }
}

@Composable
fun AddProductScreenContent(
    modifier: Modifier = Modifier,
    productId: Int,
    uiState: ProductUiState,
    saveStatus: SaveStatus,
    categories: List<String>,
    onSaveProduct: (Product) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Entradas") }
    var descuento by remember { mutableStateOf("0") }
    var esPromocion by remember { mutableStateOf(false) }
    var disponible by remember { mutableStateOf(true) }
    var productUid by remember { mutableStateOf(UUID.randomUUID().toString()) }

    LaunchedEffect(categories) {
        if (categoria == "Entradas" && categories.isNotEmpty() && !categories.contains("Entradas")) {
            categoria = categories.first()
        }
    }

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
                disponible = it.disponible
                productUid = it.uid
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Selector de Imagen Profesional
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                if (imagenUrl.isNotBlank()) {
                    AsyncImage(
                        model = imagenUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Surface(
                        modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp),
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.5f)
                    ) {
                        Icon(Icons.Default.AddCircle, null, tint = Color.White, modifier = Modifier.padding(8.dp))
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddCircle, null, modifier = Modifier.size(48.dp), tint = colorResource(R.color.esmeralda))
                        Spacer(Modifier.height(8.dp))
                        Text("Añadir Imagen", fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        OutlinedTextField(
            value = nombre, 
            onValueChange = { nombre = it }, 
            label = { Text("Nombre del Producto") }, 
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            leadingIcon = { Icon(Icons.Default.DriveFileRenameOutline, null) }
        )
        
        OutlinedTextField(
            value = precio, 
            onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) precio = it }, 
            label = { Text("Precio") }, 
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), 
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            leadingIcon = { Icon(Icons.Default.MonetizationOn, null) }
        )

        OutlinedTextField(
            value = descripcion, 
            onValueChange = { descripcion = it }, 
            label = { Text("Descripción") }, 
            modifier = Modifier.fillMaxWidth(), 
            minLines = 3,
            shape = RoundedCornerShape(16.dp),
            leadingIcon = { Icon(Icons.Default.Description, null) }
        )

        Text("Categoría", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categories.forEach { cat ->
                FilterChip(
                    selected = categoria == cat, 
                    onClick = { categoria = cat }, 
                    label = { Text(cat) }
                )
            }
        }

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = esPromocion, onCheckedChange = { esPromocion = it })
                Text("Habilitar Promoción", fontWeight = FontWeight.Medium)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = disponible, onCheckedChange = { disponible = it })
                Text("Producto Disponible", fontWeight = FontWeight.Medium)
            }
        }

        if (esPromocion) {
            OutlinedTextField(
                value = descuento, 
                onValueChange = { if (it.all { c -> c.isDigit() }) descuento = it }, 
                label = { Text("% de Descuento") }, 
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), 
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
        }

        if (saveStatus is SaveStatus.Error) {
            Text(saveStatus.message, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Button(
            enabled = nombre.isNotBlank() && precio.isNotBlank() && saveStatus !is SaveStatus.Saving,
            onClick = {
                onSaveProduct(Product(
                    id = if (productId == -1) 0 else productId, 
                    uid = productUid,
                    nombre = nombre, 
                    precio = precio.toDoubleOrNull() ?: 0.0, 
                    descripcion = descripcion, 
                    imagenUrl = imagenUrl, 
                    categoria = categoria, 
                    descuento = if (esPromocion) (descuento.toDoubleOrNull() ?: 0.0) else 0.0, 
                    esPromocion = esPromocion,
                    disponible = disponible
                ))
            },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.esmeralda)),
            shape = RoundedCornerShape(16.dp)
        ) {
            if (saveStatus is SaveStatus.Saving) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (productId == -1) "Guardar Producto" else "Actualizar Producto", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
