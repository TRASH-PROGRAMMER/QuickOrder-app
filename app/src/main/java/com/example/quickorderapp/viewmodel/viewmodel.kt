package com.example.quickorderapp.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickorderapp.data.local.entities.ProductEntity
import com.example.quickorderapp.data.repository.ProductRepository
import com.example.quickorderapp.domain.usecase.GetProductsUseCase
import com.example.quickorderapp.data.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import javax.inject.Inject
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.flow
import kotlin.collections.emptyList




@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val repository: ProductRepository
) : ViewModel() {
     val products = getProductsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun addProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.addProduct(product)
        }
    }

}