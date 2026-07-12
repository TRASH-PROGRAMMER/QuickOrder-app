package com.example.quickorderapp.data.local.dao

import androidx.room.*
import com.example.quickorderapp.data.local.entities.DailyMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyMessageDao {
    @Query("SELECT * FROM daily_messages ORDER BY fechaPublicacion DESC")
    fun getAll(): Flow<List<DailyMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: DailyMessageEntity)

    @Update
    suspend fun update(message: DailyMessageEntity)

    @Delete
    suspend fun delete(message: DailyMessageEntity)
}
