package com.example.quickorderapp.data.local.dao

import androidx.room.*
import com.example.quickorderapp.data.local.entities.PromocionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PromocionDao {
    @Query("SELECT * FROM promociones WHERE activo = 1")
    fun getActivePromotions(): Flow<List<PromocionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(promocion: PromocionEntity)

    @Update
    suspend fun update(promocion: PromocionEntity)
}
