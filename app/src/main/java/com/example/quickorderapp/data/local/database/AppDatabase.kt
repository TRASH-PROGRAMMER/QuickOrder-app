package com.example.quickorderapp.data.local.database
import androidx.room.Database
import com.example.quickorderapp.data.local.entities.OrderDetailEntity
import com.example.quickorderapp.data.local.entities.OrderEntity
import com.example.quickorderapp.data.local.entities.ProductEntity
import androidx.room.RoomDatabase
import com.example.quickorderapp.data.local.dao.ProductDao
import com.example.quickorderapp.data.local.dao.OrderDao



@Database(
    entities = [ProductEntity::class, OrderEntity::class, OrderDetailEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
}

