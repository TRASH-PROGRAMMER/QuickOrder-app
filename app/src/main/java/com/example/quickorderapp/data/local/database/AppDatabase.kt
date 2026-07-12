package com.example.quickorderapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quickorderapp.data.local.dao.*
import com.example.quickorderapp.data.local.entities.*

/**
 * Base de datos principal de Room para QuickOrder.
 */
@Database(
    entities = [
        ProductEntity::class,
        OrderEntity::class,
        OrderDetailEntity::class,
        UserEntity::class,
        MesaEntity::class,
        PromocionEntity::class,
        VentaEntity::class,
        CategoryEntity::class,
        DailyMessageEntity::class,
        RestaurantInfoEntity::class
    ],
    version = 8, // Incrementado por campo orderNumber en OrderEntity
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun orderDetailDao(): OrderDetailDao
    abstract fun userDao(): UserDao
    abstract fun mesaDao(): MesaDao
    abstract fun promocionDao(): PromocionDao
    abstract fun ventaDao(): VentaDao
    abstract fun categoryDao(): CategoryDao
    abstract fun dailyMessageDao(): DailyMessageDao
    abstract fun restaurantInfoDao(): RestaurantInfoDao
}
