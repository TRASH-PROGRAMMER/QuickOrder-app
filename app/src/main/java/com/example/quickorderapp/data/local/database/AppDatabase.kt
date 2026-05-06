package com.example.quickorderapp.data.local.database
import androidx.room.Database
import com.example.quickorderapp.data.local.entities.OrderDetailEntity
import com.example.quickorderapp.data.local.entities.OrderEntity
import com.example.quickorderapp.data.local.entities.ProductEntity
import androidx.room.RoomDatabase
import com.example.quickorderapp.data.local.dao.ProductDao
import com.example.quickorderapp.data.local.dao.OrderDao



/*
La base de datos principal [RoomDatabase] para la aplicación Pedido Rápido.

 *
 * Esta clase define la configuración de la base de datos y sirve como punto de acceso principal para

 * los datos persistentes, gestionando entidades como [ProductEntity], [OrderEntity] y [OrderDetailEntity].

 *
 * @property productDao Proporciona acceso a las operaciones de datos relacionadas con el producto.

 * @property orderDao Proporciona acceso a las operaciones de datos relacionadas con el pedido y sus detalles.
 */
@Database(
    entities = [ProductEntity::class, OrderEntity::class, OrderDetailEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
}

