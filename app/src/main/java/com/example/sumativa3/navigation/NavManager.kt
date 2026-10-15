package com.example.sumativa3.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sumativa3.vistas.AjustesView
import com.example.sumativa3.vistas.ListadoActivosView
import com.example.sumativa3.vistas.RegistrarActivoView
import com.example.sumativa3.viewmodel.InventarioViewModel

@Composable
fun NavManager(viewModel: InventarioViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BarraInferior(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destino.Listado.ruta,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destino.Listado.ruta) { ListadoActivosView(viewModel) }
            composable(Destino.Registrar.ruta) { RegistrarActivoView(viewModel) }
            composable(Destino.Ajustes.ruta) { AjustesView(viewModel) }
        }
    }
}