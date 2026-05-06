package com.example.quickorderapp.di

import com.example.quickorderapp.data.repository.ProductRepository
import com.example.quickorderapp.data.repository.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Dagger Hilt que proporciona enlaces de inyección de dependencias para repositorios.
 *
 * *
 * * Este módulo se encarga de vincular la interfaz [ProductRepository] con su
 * * implementación concreta, [RepositoryImpl]. Se instala en el [SingletonComponent],
 * * asegurando que las instancias del repositorio se mantengan como singletons durante todo el
 * * ciclo de vida de la aplicación.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        repositoryImpl: RepositoryImpl
    ): ProductRepository
}
