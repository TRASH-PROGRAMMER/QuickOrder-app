package com.example.quickorderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickorderapp.domain.model.Product
import com.example.quickorderapp.domain.usecase.AddProductUseCase
import com.example.quickorderapp.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Estado de la UI para la pantalla de productos.
 */
sealed interface ProductUiState {
    data object Loading : ProductUiState
    data class Success(val products: List<Product>) : ProductUiState
    data class Error(val message: String) : ProductUiState
}

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val addProductUseCase: AddProductUseCase
) : ViewModel() {

    // Estado de la UI expuesto mediante StateFlow
    val uiState: StateFlow<ProductUiState> = getProductsUseCase()
        .map { products ->
            if (products.isEmpty()) ProductUiState.Loading // O un estado vacío si prefieres
            else ProductUiState.Success(products)
        }
        .catch { e -> emit(ProductUiState.Error(e.message ?: "Error desconocido")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProductUiState.Loading
        )

    fun addProduct(product: Product) {
        viewModelScope.launch {
            try {
                addProductUseCase(product)
            } catch (e: Exception) {
                // Manejar error de inserción si es necesario
            }
        }
    }
}
