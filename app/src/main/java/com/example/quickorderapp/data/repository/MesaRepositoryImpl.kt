package com.example.quickorderapp.data.repository

import com.example.quickorderapp.data.local.dao.MesaDao
import com.example.quickorderapp.data.remote.firebase.FirebaseMesaDataSource
import com.example.quickorderapp.data.remote.firebase.FirebaseSyncManager
import com.example.quickorderapp.domain.model.Mesa
import com.example.quickorderapp.domain.repository.MesaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MesaRepositoryImpl @Inject constructor(
    private val mesaDao: MesaDao,
    private val firebaseMesaDataSource: FirebaseMesaDataSource,
    private val syncManager: FirebaseSyncManager
) : MesaRepository {

    init {
        // Iniciamos la sincronización en tiempo real al instanciar el repositorio
        firebaseMesaDataSource.startRealtimeMesaSync(kotlinx.coroutines.CoroutineScope(Dispatchers.IO))
    }

    override fun getMesas(): Flow<List<Mesa>> {
        if (syncManager.hasInternetConnection()) {
            syncManager.syncAll()
        }
        return mesaDao.getAll().map { it.toMesaDomainList() }
    }

    override suspend fun addMesa(mesa: Mesa) = withContext(Dispatchers.IO) {
        mesaDao.insert(mesa.toEntity())
        if (syncManager.hasInternetConnection()) {
            firebaseMesaDataSource.addMesa(mesa)
        }
    }

    override suspend fun updateMesa(mesa: Mesa) = withContext(Dispatchers.IO) {
        mesaDao.update(mesa.toEntity())
        if (syncManager.hasInternetConnection()) {
            firebaseMesaDataSource.addMesa(mesa) // .set(merge=true)
        }
    }

    override suspend fun deleteMesa(mesa: Mesa) = withContext(Dispatchers.IO) {
        mesaDao.delete(mesa.toEntity())
        if (syncManager.hasInternetConnection()) {
            firebaseMesaDataSource.deleteMesa(mesa)
        }
    }
}
