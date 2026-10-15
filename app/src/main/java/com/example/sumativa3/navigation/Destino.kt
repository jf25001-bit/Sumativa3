package com.example.sumativa3.navigation

sealed class Destino(val ruta: String, val titulo: String) {
    object Listado : Destino("listado", "Listado de Activos")
    object Registrar : Destino("registrar", "Registrar Activo")
    object Ajustes : Destino("ajustes", "Perfil / Ajustes")
}