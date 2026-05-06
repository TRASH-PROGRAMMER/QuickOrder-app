package com.example.quickorderapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
Representa un registro de producto dentro de la base de datos local de Room.

 * Esta entidad define la estructura de la tabla "productos".

 *
 * @property id Identificador único del producto, generado automáticamente por la base de datos.

 * @property nombre Nombre del producto.

 * @property precio Precio unitario del producto.

 * @property descripcion Descripción detallada de las características del producto.

 * @property imagenUrl URL o ruta local de la imagen representativa del producto.

 * @property categoria Clasificación o categoría del producto.

 * @property descuento Descuento aplicable al producto.
 */
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) 
    val id: Int = 0,
    val nombre: String,
    val precio: Double,
    val descripcion: String,
    val imagenUrl: String = "",
    val categoria: String = "",
    val descuento: Double = 0.0
)
