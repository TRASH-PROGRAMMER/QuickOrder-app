package com.example.quickorderapp.data.remote.firebase

import android.util.Log
import com.example.quickorderapp.domain.model.Order
import com.example.quickorderapp.domain.model.OrderItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseOrderDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    companion object {
        private const val TAG = "FirebaseOrderDS"
        private const val COLLECTION_ORDERS = "orders"
    }

    suspend fun saveOrder(order: Order): String {
        return try {
            val orderRef = if (order.remoteId.isNotEmpty()) {
                firestore.collection(COLLECTION_ORDERS).document(order.remoteId)
            } else {
                firestore.collection(COLLECTION_ORDERS).document()
            }

            val orderMap = hashMapOf(
                "id" to orderRef.id,
                "orderNumber" to order.orderNumber,
                "userEmail" to order.userEmail,
                "clienteNombre" to order.clienteNombre,
                "clienteId" to order.clienteId,
                "numeroMesa" to order.numeroMesa,
                "total" to order.total,
                "estado" to order.estado,
                "fecha" to order.fecha,
                "notas" to order.notas,
                "items" to order.items.map {
                    hashMapOf(
                        "productoId" to it.productoId,
                        "productoNombre" to it.productoNombre,
                        "cantidad" to it.cantidad,
                        "precioUnitario" to it.precioUnitario,
                        "subtotal" to it.subtotal
                    )
                }
            )

            orderRef.set(orderMap, SetOptions.merge()).await()
            orderRef.id
        } catch (e: Exception) {
            Log.e(TAG, "Error saving order to Firebase", e)
            ""
        }
    }

    suspend fun getOrdersByUser(email: String): List<Order> {
        return try {
            val snapshot = firestore.collection(COLLECTION_ORDERS)
                .whereEqualTo("userEmail", email)
                .get()
                .await()
            
            snapshot.documents.mapNotNull { doc ->
                val items = (doc.get("items") as? List<Map<String, Any>>)?.map {
                    OrderItem(
                        productoId = (it["productoId"] as? Long)?.toInt() ?: 0,
                        productoNombre = it["productoNombre"] as? String ?: "",
                        cantidad = (it["cantidad"] as? Long)?.toInt() ?: 0,
                        precioUnitario = (it["precioUnitario"] as? Double) ?: 0.0,
                        subtotal = (it["subtotal"] as? Double) ?: 0.0
                    )
                } ?: emptyList()

                Order(
                    remoteId = doc.id,
                    orderNumber = doc.getLong("orderNumber")?.toInt() ?: 0,
                    userEmail = doc.getString("userEmail") ?: "",
                    clienteNombre = doc.getString("clienteNombre") ?: "",
                    clienteId = doc.getString("clienteId") ?: "",
                    numeroMesa = doc.getLong("numeroMesa")?.toInt() ?: 0,
                    total = doc.getDouble("total") ?: 0.0,
                    estado = doc.getString("estado") ?: "PENDIENTE",
                    fecha = doc.getLong("fecha") ?: 0L,
                    notas = doc.getString("notas") ?: "",
                    items = items
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching orders from Firebase", e)
            emptyList()
        }
    }

    suspend fun getAllOrders(): List<Order> {
        return try {
            val snapshot = firestore.collection(COLLECTION_ORDERS)
                .orderBy("fecha", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()
            
            snapshot.documents.mapNotNull { doc ->
                val items = (doc.get("items") as? List<Map<String, Any>>)?.map {
                    OrderItem(
                        productoId = (it["productoId"] as? Long)?.toInt() ?: 0,
                        productoNombre = it["productoNombre"] as? String ?: "",
                        cantidad = (it["cantidad"] as? Long)?.toInt() ?: 0,
                        precioUnitario = (it["precioUnitario"] as? Double) ?: 0.0,
                        subtotal = (it["subtotal"] as? Double) ?: 0.0
                    )
                } ?: emptyList()

                Order(
                    remoteId = doc.id,
                    orderNumber = doc.getLong("orderNumber")?.toInt() ?: 0,
                    userEmail = doc.getString("userEmail") ?: "",
                    clienteNombre = doc.getString("clienteNombre") ?: "",
                    clienteId = doc.getString("clienteId") ?: "",
                    numeroMesa = doc.getLong("numeroMesa")?.toInt() ?: 0,
                    total = doc.getDouble("total") ?: 0.0,
                    estado = doc.getString("estado") ?: "PENDIENTE",
                    fecha = doc.getLong("fecha") ?: 0L,
                    notas = doc.getString("notas") ?: "",
                    items = items
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching all orders from Firebase", e)
            emptyList()
        }
    }

    suspend fun updateOrderStatus(remoteId: String, newStatus: String) {
        try {
            firestore.collection(COLLECTION_ORDERS)
                .document(remoteId)
                .update("estado", newStatus)
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Error updating order status in Firebase", e)
        }
    }
}
