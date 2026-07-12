package com.example.quickorderapp.domain.usecase

import com.example.quickorderapp.domain.model.Category
import com.example.quickorderapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<Category>> = repository.getCategories()
}
