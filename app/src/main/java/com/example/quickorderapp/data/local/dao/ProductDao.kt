package com.example.quickorderapp.data.local.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.quickorderapp.data.local.entities.ProductEntity
import androidx.room.Delete



@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
     fun getAll(): Flow<List<ProductEntity>>
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertAll(products: ProductEntity)
     @Delete
     suspend fun delete(product: ProductEntity)
}