package com.example.sumativa3.data

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.inventarioDataStore by preferencesDataStore(name = "inventario_preferencias")

object PreferenciasInventario {
    val CODIGO_AUDITOR = stringPreferencesKey("codigo_auditor")
    val ULTIMA_UBICACION = stringPreferencesKey("ultima_ubicacion")
    val MODO_VISTA = stringPreferencesKey("modo_vista") // "lista" o "cuadricula"
}