package com.example.quickorderapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.quickorderapp.data.local.dao.*
import com.example.quickorderapp.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para proporcionar la base de datos y sus DAOs.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "quick_order_database_v10" // Incrementado para forzar la creación con el nuevo esquema (orderNumber)
        )
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Insertamos usuarios "maestros" al crear la base de datos por primera vez
                db.execSQL("INSERT INTO usuarios (nombre, correo, rol, password) VALUES ('Administrador', 'admin@quickorder.com', 'ADMIN', 'Admin123')")
                db.execSQL("INSERT INTO usuarios (nombre, correo, rol, password) VALUES ('Mesero Principal', 'mesero@quickorder.com', 'MESERO', 'Mesero123')")
            }
        })
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideProductDao(database: AppDatabase): ProductDao = database.productDao()

    @Provides
    fun provideOrderDao(database: AppDatabase): OrderDao = database.orderDao()

    @Provides
    fun provideOrderDetailDao(database: AppDatabase): OrderDetailDao = database.orderDetailDao()

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    fun provideMesaDao(database: AppDatabase): MesaDao = database.mesaDao()

    @Provides
    fun providePromocionDao(database: AppDatabase): PromocionDao = database.promocionDao()

    @Provides
    fun provideVentaDao(database: AppDatabase): VentaDao = database.ventaDao()

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun provideDailyMessageDao(database: AppDatabase): DailyMessageDao = database.dailyMessageDao()

    @Provides
    fun provideRestaurantInfoDao(database: AppDatabase): RestaurantInfoDao = database.restaurantInfoDao()
}
