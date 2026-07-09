package com.example.quickorderapp.domain.repository

import com.example.quickorderapp.domain.model.Mesa
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz de repositorio para la gestión de mesas.
 */
interface MesaRepository {
    fun getMesas(): Flow<List<Mesa>>
    suspend fun addMesa(mesa: Mesa)
    suspend fun updateMesa(mesa: Mesa)
    suspend fun deleteMesa(mesa: Mesa)
}
