package com.example.quickorderapp.data.local.dao

import androidx.room.*
import com.example.quickorderapp.data.local.entities.MesaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MesaDao {
    @Query("SELECT * FROM mesas ORDER BY numero ASC")
    fun getAll(): Flow<List<MesaEntity>>

    @Query("SELECT * FROM mesas")
    suspend fun getAllList(): List<MesaEntity>

    @Query("SELECT * FROM mesas WHERE numero = :numero LIMIT 1")
    suspend fun getByNumero(numero: Int): MesaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mesa: MesaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mesas: List<MesaEntity>)

    @Update
    suspend fun update(mesa: MesaEntity)

    @Delete
    suspend fun delete(mesa: MesaEntity)
}
