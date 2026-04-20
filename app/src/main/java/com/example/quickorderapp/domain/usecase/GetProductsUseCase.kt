package com.example.quickorderapp.domain.usecase
import com.example.quickorderapp.data.repository.ProductRepository
import javax.inject.Inject

 class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
     operator fun invoke()= productRepository.getProducts()
 }
