package com.example.quickorderapp.data.local.dao

import androidx.room.*
import com.example.quickorderapp.data.local.entities.OrderEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para la gestión de pedidos.
 */
@Dao
interface OrderDao {
    @Query("SELECT * FROM pedidos ORDER BY fecha DESC")
    fun getAll(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM pedidos WHERE userEmail = :email ORDER BY fecha DESC")
    fun getByUserEmail(email: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM pedidos WHERE id = :id")
    fun getByIdFlow(id: Int): Flow<OrderEntity?>

    @Query("SELECT * FROM pedidos WHERE id = :id")
    suspend fun getById(id: Int): OrderEntity?

    @Query("SELECT MAX(orderNumber) FROM pedidos")
    suspend fun getMaxOrderNumber(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: OrderEntity): Long

    @Update
    suspend fun update(order: OrderEntity)

    @Delete
    suspend fun delete(order: OrderEntity)
}
