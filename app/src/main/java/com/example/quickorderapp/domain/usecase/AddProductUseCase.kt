package com.example.quickorderapp.domain.usecase

import com.example.quickorderapp.domain.model.Product
import com.example.quickorderapp.domain.repository.ProductRepository
import javax.inject.Inject

/**
 * Caso de uso para añadir un nuevo producto.
 */
class AddProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product) {
        productRepository.addProduct(product)
    }
}
