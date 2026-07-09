package com.example.quickorderapp.data.local.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow
import com.example.quickorderapp.data.local.entities.ProductEntity



/*
 * Objeto de acceso a datos (DAO) para gestionar las entradas de [ProductEntity] en la base de datos local.
 * Proporciona métodos para realizar operaciones CRUD en la tabla de productos.
 */
@Dao
interface ProductDao {
    @Query("SELECT * FROM productos")
     fun getAll(): Flow<List<ProductEntity>>
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insert(product: ProductEntity)

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertAll(products: List<ProductEntity>)

     @Query("SELECT * FROM productos WHERE nombre = :nombre LIMIT 1")
     suspend fun getByNombre(nombre: String): ProductEntity?

     @Update
     suspend fun update(product: ProductEntity)

     @Delete
     suspend fun delete(product: ProductEntity)
}