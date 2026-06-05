package com.example.quickorderapp.domain.usecase

import com.example.quickorderapp.domain.model.Product
import com.example.quickorderapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para recuperar la lista de productos del [ProductRepository].
 * Devuelve modelos de dominio [Product].
 */
class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> = productRepository.getProducts()
}
