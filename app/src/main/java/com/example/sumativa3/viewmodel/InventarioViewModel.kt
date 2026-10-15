package com.example.sumativa3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sumativa3.data.ActivoFijo
import com.example.sumativa3.data.InventarioDatabase
import com.example.sumativa3.repository.ActivoRepository
import com.example.sumativa3.repository.PreferenciasRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventarioViewModel(application: Application) : AndroidViewModel(application) {
    private val activoRepository: ActivoRepository
    private val preferenciasRepository = PreferenciasRepository(application)

    val listaActivos: StateFlow<List<ActivoFijo>>
    val codigoAuditor: StateFlow<String>
    val ultimaUbicacion: StateFlow<String>
    val modoVista: StateFlow<String>

    init {
        val dao = InventarioDatabase.obtenerBaseDatos(application).activoDao()
        activoRepository = ActivoRepository(dao)

        listaActivos = activoRepository.todosLosActivos.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )
        codigoAuditor = preferenciasRepository.codigoAuditor.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), ""
        )
        ultimaUbicacion = preferenciasRepository.ultimaUbicacion.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), ""
        )
        modoVista = preferenciasRepository.modoVista.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), "lista"
        )
    }

    fun registrarActivo(activo: ActivoFijo, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                activoRepository.insertar(activo)
                preferenciasRepository.actualizarUltimaUbicacion(activo.ubicacion)
                onResult(true, null)
            } catch (e: Exception) {
                onResult(false, "Error: El código de inventario ya existe.")
            }
        }
    }

    fun guardarAjustes(codigo: String, ubicacion: String) {
        viewModelScope.launch {
            preferenciasRepository.actualizarCodigoAuditor(codigo)
            preferenciasRepository.actualizarUltimaUbicacion(ubicacion)
        }
    }

    fun cambiarModoVista(modo: String) {
        viewModelScope.launch {
            preferenciasRepository.actualizarModoVista(modo)
        }
    }
}