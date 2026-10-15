package com.example.sumativa3.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivoDao {
    @Query("SELECT * FROM activos")
    fun obtenerTodosLosActivos(): Flow<List<ActivoFijo>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertarActivo(activo: ActivoFijo)
}