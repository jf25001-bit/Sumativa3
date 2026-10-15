package com.example.sumativa3.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activos")
data class ActivoFijo(
    @PrimaryKey val codigoInventario: String,
    val nombre: String,
    val categoria: String,
    val ubicacion: String,
    val estadoFisico: String,
    val fechaAdquisition: String,
    val fotoUri: String,
    val codigoAuditor: String,
    val fechaRegistro: String
)