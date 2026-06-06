package com.example.quickorderapp.di

import android.content.Context
import androidx.room.Room
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
            "quick_order_database_v2" // Cambiado para evitar conflictos de esquema
        )
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
}
