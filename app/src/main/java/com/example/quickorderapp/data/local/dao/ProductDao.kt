package com.example.quickorderapp.data.local.dao

import androidx.room.*
import com.example.quickorderapp.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para la gestión de productos en Room.
 */
@Dao
interface ProductDao {
    @Query("SELECT * FROM productos")
    fun getAll(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)

    @Query("SELECT * FROM productos WHERE uid = :uid LIMIT 1")
    suspend fun getByUid(uid: String): ProductEntity?

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    fun getByIdFlow(id: Int): Flow<ProductEntity?>

    @Update
    suspend fun update(product: ProductEntity)

    @Delete
    suspend fun delete(product: ProductEntity)
}
