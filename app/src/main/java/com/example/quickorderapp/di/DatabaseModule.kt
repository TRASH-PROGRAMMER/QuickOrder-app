package com.example.quickorderapp.di

import android.content.Context
import androidx.room.Room
import com.example.quickorderapp.data.local.dao.OrderDao
import com.example.quickorderapp.data.local.dao.ProductDao
import com.example.quickorderapp.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**

 * Módulo Dagger Hilt que proporciona dependencias relacionadas con la base de datos.
 * Este módulo se encarga de inicializar la instancia de la base de datos Room y
 * proporcionar los objetos de acceso a datos (DAO) necesarios para la persistencia de datos local.
 * Todas las dependencias proporcionadas aquí están limitadas al [SingletonComponent].
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
            "quick_order_db"
        ).build()
    }

    @Provides
    fun provideProductDao(database: AppDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    fun provideOrderDao(database: AppDatabase): OrderDao {
        return database.orderDao()
    }
}
