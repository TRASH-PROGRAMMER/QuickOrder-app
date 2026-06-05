package com.example.quickorderapp.data.local.dao

import androidx.room.*
import com.example.quickorderapp.data.local.entities.OrderDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDetailDao {
    @Query("SELECT * FROM detalle_pedidos WHERE pedidoId = :pedidoId")
    fun getDetailsByOrderId(pedidoId: Int): Flow<List<OrderDetailEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(details: List<OrderDetailEntity>)

    @Delete
    suspend fun delete(detail: OrderDetailEntity)
}
