package com.example.quickorderapp.domain.usecase

import com.example.quickorderapp.domain.model.Product
import com.example.quickorderapp.domain.repository.ProductRepository
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: Product) = repository.updateProduct(product)
}
