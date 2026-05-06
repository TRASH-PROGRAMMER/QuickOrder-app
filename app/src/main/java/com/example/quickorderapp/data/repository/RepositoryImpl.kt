package com.example.quickorderapp.data.repository

import com.example.quickorderapp.data.local.dao.ProductDao
import com.example.quickorderapp.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementación de la interfaz [ProductRepository] que sirve como punto de entrada de la capa de datos

 * para gestionar las operaciones relacionadas con el producto.

 *
 * Esta clase coordina el flujo de datos entre la base de datos local proporcionada por [ProductDao]

 * y las capas de dominio/interfaz de usuario de la aplicación.

 *
 * @property productDao Objeto de acceso a datos utilizado para realizar operaciones CRUD en la tabla de productos.
 */
class RepositoryImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductRepository {
    
    override fun getProducts(): Flow<List<ProductEntity>> {
            return productDao.getAll()
    }

    override suspend fun addProduct(product: ProductEntity) {
        // Corregido: El método en tu ProductDao es 'insertAll'
        productDao.insertAll(product)
    }
}
