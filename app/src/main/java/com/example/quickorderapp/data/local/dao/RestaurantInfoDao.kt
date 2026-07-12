package com.example.quickorderapp.data.local.dao

import androidx.room.*
import com.example.quickorderapp.data.local.entities.RestaurantInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantInfoDao {
    @Query("SELECT * FROM restaurant_info WHERE id = 'rest_info'")
    fun getInfo(): Flow<RestaurantInfoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(info: RestaurantInfoEntity)
}
