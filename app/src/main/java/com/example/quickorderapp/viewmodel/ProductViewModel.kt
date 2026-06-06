package com.example.quickorderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickorderapp.domain.model.Product
import com.example.quickorderapp.domain.usecase.AddProductUseCase
import com.example.quickorderapp.domain.usecase.DeleteProductUseCase
import com.example.quickorderapp.domain.usecase.GetProductsUseCase
import com.example.quickorderapp.domain.usecase.UpdateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ProductUiState {
    data object Loading : ProductUiState
    data class Success(val products: List<Product>) : ProductUiState
    data class Error(val message: String) : ProductUiState
}

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase
) : ViewModel() {

    val uiState: StateFlow<ProductUiState> = getProductsUseCase()
        .map<List<Product>, ProductUiState> { products -> 
            ProductUiState.Success(products) 
        }
        .catch { e -> 
            e.printStackTrace()
            emit(ProductUiState.Error("Fallo en BD: ${e.localizedMessage}")) 
        }
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
                // Error al insertar
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            try {
                updateProductUseCase(product)
            } catch (e: Exception) {
                // Error al actualizar
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            try {
                deleteProductUseCase(product)
            } catch (e: Exception) {
                // Error al eliminar
            }
        }
    }
}
