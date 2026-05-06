package com.example.quickorderapp.domain.usecase
import com.example.quickorderapp.data.repository.ProductRepository
import javax.inject.Inject

/**

 * Caso de uso para recuperar la lista de productos del [ProductRepository].

 *
 * Esta clase sirve como punto de entrada a la capa de dominio para obtener datos de productos,

 * abstraiendo la fuente de datos de las capas de interfaz de usuario o modelo de vista.

 *
 * @property productRepository El repositorio utilizado para acceder a los datos de productos.

 */
 class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
     operator fun invoke()= productRepository.getProducts()
 }
