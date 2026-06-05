package com.example.quickorderapp.data.local.dao

import androidx.room.*
import com.example.quickorderapp.data.local.entities.VentaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VentaDao {
    @Query("SELECT * FROM ventas ORDER BY fecha DESC")
    fun getAll(): Flow<List<VentaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(venta: VentaEntity)
}
