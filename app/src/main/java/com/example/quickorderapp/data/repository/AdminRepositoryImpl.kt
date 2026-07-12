package com.example.quickorderapp.data.repository

import com.example.quickorderapp.data.local.dao.CategoryDao
import com.example.quickorderapp.data.local.dao.DailyMessageDao
import com.example.quickorderapp.data.local.dao.RestaurantInfoDao
import com.example.quickorderapp.data.remote.firebase.FirebaseCategoryDataSource
import com.example.quickorderapp.data.remote.firebase.FirebaseDailyMessageDataSource
import com.example.quickorderapp.data.remote.firebase.FirebaseRestaurantInfoDataSource
import com.example.quickorderapp.data.remote.firebase.FirebaseSyncManager
import com.example.quickorderapp.domain.model.Category
import com.example.quickorderapp.domain.model.DailyMessage
import com.example.quickorderapp.domain.model.RestaurantInfo
import com.example.quickorderapp.domain.repository.AdminRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val dailyMessageDao: DailyMessageDao,
    private val restaurantInfoDao: RestaurantInfoDao,
    private val firebaseCategoryDataSource: FirebaseCategoryDataSource,
    private val firebaseDailyMessageDataSource: FirebaseDailyMessageDataSource,
    private val firebaseRestaurantInfoDataSource: FirebaseRestaurantInfoDataSource,
    private val syncManager: FirebaseSyncManager
) : AdminRepository {

    override fun getCategories(): Flow<List<Category>> {
        return categoryDao.getAll().map { it.toCategoryDomainList() }
    }

    override suspend fun addCategory(category: Category) = withContext(Dispatchers.IO) {
        categoryDao.insert(category.toEntity())
        if (syncManager.hasInternetConnection()) {
            firebaseCategoryDataSource.addCategory(category)
        }
    }

    override suspend fun updateCategory(category: Category) = withContext(Dispatchers.IO) {
        categoryDao.update(category.toEntity())
        if (syncManager.hasInternetConnection()) {
            firebaseCategoryDataSource.addCategory(category)
        }
    }

    override suspend fun deleteCategory(category: Category) = withContext(Dispatchers.IO) {
        categoryDao.delete(category.toEntity())
        if (syncManager.hasInternetConnection()) {
            firebaseCategoryDataSource.deleteCategory(category)
        }
    }

    override fun getDailyMessages(): Flow<List<DailyMessage>> {
        return dailyMessageDao.getAll().map { it.toDailyMessageDomainList() }
    }

    override suspend fun saveDailyMessage(message: DailyMessage) = withContext(Dispatchers.IO) {
        dailyMessageDao.insert(message.toEntity())
        if (syncManager.hasInternetConnection()) {
            firebaseDailyMessageDataSource.saveMessage(message)
        }
    }

    override suspend fun deleteDailyMessage(message: DailyMessage) = withContext(Dispatchers.IO) {
        dailyMessageDao.delete(message.toEntity())
        if (syncManager.hasInternetConnection()) {
            firebaseDailyMessageDataSource.deleteMessage(message)
        }
    }

    override fun getRestaurantInfo(): Flow<RestaurantInfo?> {
        return restaurantInfoDao.getInfo().map { it?.toDomain() }
    }

    override suspend fun saveRestaurantInfo(info: RestaurantInfo) = withContext(Dispatchers.IO) {
        restaurantInfoDao.insert(info.toEntity())
        if (syncManager.hasInternetConnection()) {
            firebaseRestaurantInfoDataSource.saveInfo(info)
        }
    }

    override suspend fun getOrderStats(): Map<String, Int> {
        // Implementación futura o placeholder
        return emptyMap()
    }
}
