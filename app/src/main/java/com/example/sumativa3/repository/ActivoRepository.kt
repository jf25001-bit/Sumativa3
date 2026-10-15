package com.example.sumativa3.repository

import com.example.sumativa3.data.ActivoDao
import com.example.sumativa3.data.ActivoFijo
import kotlinx.coroutines.flow.Flow

class ActivoRepository(private val activoDao: ActivoDao) {
    val todosLosActivos: Flow<List<ActivoFijo>> = activoDao.obtenerTodosLosActivos()

    suspend fun insertar(activo: ActivoFijo) {
        activoDao.insertarActivo(activo)
    }
}