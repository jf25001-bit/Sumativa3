package com.example.sumativa3.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.sumativa3.data.PreferenciasInventario
import com.example.sumativa3.data.inventarioDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenciasRepository(private val context: Context) {

    val codigoAuditor: Flow<String> = context.inventarioDataStore.data
        .map { prefs -> prefs[PreferenciasInventario.CODIGO_AUDITOR] ?: "" }

    val ultimaUbicacion: Flow<String> = context.inventarioDataStore.data
        .map { prefs -> prefs[PreferenciasInventario.ULTIMA_UBICACION] ?: "" }

    val modoVista: Flow<String> = context.inventarioDataStore.data
        .map { prefs -> prefs[PreferenciasInventario.MODO_VISTA] ?: "lista" }

    suspend fun actualizarCodigoAuditor(codigo: String) {
        context.inventarioDataStore.edit { prefs ->
            prefs[PreferenciasInventario.CODIGO_AUDITOR] = codigo
        }
    }

    suspend fun actualizarUltimaUbicacion(ubicacion: String) {
        context.inventarioDataStore.edit { prefs ->
            prefs[PreferenciasInventario.ULTIMA_UBICACION] = ubicacion
        }
    }

    suspend fun actualizarModoVista(modo: String) {
        context.inventarioDataStore.edit { prefs ->
            prefs[PreferenciasInventario.MODO_VISTA] = modo
        }
    }
}