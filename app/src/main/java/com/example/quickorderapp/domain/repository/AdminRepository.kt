package com.example.quickorderapp.domain.repository

import com.example.quickorderapp.domain.model.Category
import com.example.quickorderapp.domain.model.DailyMessage
import com.example.quickorderapp.domain.model.RestaurantInfo
import kotlinx.coroutines.flow.Flow

interface AdminRepository {
    // Categories
    fun getCategories(): Flow<List<Category>>
    suspend fun addCategory(category: Category)
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)

    // Daily Message
    fun getDailyMessages(): Flow<List<DailyMessage>>
    suspend fun saveDailyMessage(message: DailyMessage)
    suspend fun deleteDailyMessage(message: DailyMessage)

    // Restaurant Info
    fun getRestaurantInfo(): Flow<RestaurantInfo?>
    suspend fun saveRestaurantInfo(info: RestaurantInfo)
    
    // Statistics (placeholder for now)
    suspend fun getOrderStats(): Map<String, Int>
}
