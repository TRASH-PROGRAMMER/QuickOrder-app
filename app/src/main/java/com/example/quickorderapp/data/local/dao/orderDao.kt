package com.example.quickorderapp.data.local.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.quickorderapp.data.local.entities.OrderEntity
import androidx.room.Delete


@Dao
interface OrderDao {
    @Query("SELECT * FROM orders")
    fun getAll(): Flow<List<OrderEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: OrderEntity)
    @Delete
    suspend fun delete(order: OrderEntity)
}