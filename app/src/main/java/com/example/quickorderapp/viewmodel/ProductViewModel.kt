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

sealed interface SaveStatus {
    data object Idle : SaveStatus
    data object Saving : SaveStatus
    data object Success : SaveStatus
    data class Error(val message: String) : SaveStatus
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
            emit(ProductUiState.Error("Fallo en BD: ${e.localizedMessage}")) 
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProductUiState.Loading
        )

    private val _saveStatus = MutableStateFlow<SaveStatus>(SaveStatus.Idle)
    val saveStatus: StateFlow<SaveStatus> = _saveStatus

    fun addProduct(product: Product) {
        viewModelScope.launch {
            _saveStatus.value = SaveStatus.Saving
            try {
                addProductUseCase(product)
                _saveStatus.value = SaveStatus.Success
            } catch (e: Exception) {
                _saveStatus.value = SaveStatus.Error(e.localizedMessage ?: "Error al guardar")
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            _saveStatus.value = SaveStatus.Saving
            try {
                updateProductUseCase(product)
                _saveStatus.value = SaveStatus.Success
            } catch (e: Exception) {
                _saveStatus.value = SaveStatus.Error(e.localizedMessage ?: "Error al actualizar")
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

    fun resetSaveStatus() {
        _saveStatus.value = SaveStatus.Idle
    }
}
