package com.example.quickorderapp.di

import com.example.quickorderapp.data.repository.AdminRepositoryImpl
import com.example.quickorderapp.data.repository.AuthRepositoryImpl
import com.example.quickorderapp.data.repository.MesaRepositoryImpl
import com.example.quickorderapp.data.repository.OrderRepositoryImpl
import com.example.quickorderapp.data.repository.ProductRepositoryImpl
import com.example.quickorderapp.domain.repository.AdminRepository
import com.example.quickorderapp.domain.repository.AuthRepository
import com.example.quickorderapp.domain.repository.MesaRepository
import com.example.quickorderapp.domain.repository.OrderRepository
import com.example.quickorderapp.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para la inyección de repositorios.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindMesaRepository(
        mesaRepositoryImpl: MesaRepositoryImpl
    ): MesaRepository

    @Binds
    @Singleton
    abstract fun bindAdminRepository(
        adminRepositoryImpl: AdminRepositoryImpl
    ): AdminRepository

    @Binds
    @Singleton
    abstract fun bindOrderRepository(
        orderRepositoryImpl: OrderRepositoryImpl
    ): OrderRepository
}
