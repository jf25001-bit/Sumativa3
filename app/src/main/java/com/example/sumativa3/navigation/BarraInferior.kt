package com.example.sumativa3.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BarraInferior(navController: NavController) {
    val items = listOf(Destino.Listado, Destino.Registrar, Destino.Ajustes)
    NavigationBar {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val rutaActual = navBackStackEntry.value?.destination?.route

        items.forEach { destino ->
            NavigationBarItem(
                icon = {
                    when (destino) {
                        Destino.Listado -> Icon(Icons.Default.List, contentDescription = null)
                        Destino.Registrar -> Icon(Icons.Default.Add, contentDescription = null)
                        Destino.Ajustes -> Icon(Icons.Default.Settings, contentDescription = null)
                    }
                },
                label = { Text(destino.titulo) },
                selected = rutaActual == destino.ruta,
                onClick = {
                    if (rutaActual != destino.ruta) {
                        navController.navigate(destino.ruta) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}