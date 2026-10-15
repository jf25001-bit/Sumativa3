package com.example.sumativa3.vistas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sumativa3.viewmodel.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListadoActivosView(viewModel: InventarioViewModel) {
    val activos by viewModel.listaActivos.collectAsState()
    val modoVista by viewModel.modoVista.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Activos Registrados: ${activos.size}") },
                actions = {
                    IconButton(onClick = {
                        val nuevoModo = if (modoVista == "lista") "cuadricula" else "lista"
                        viewModel.cambiarModoVista(nuevoModo)
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Cambiar vista")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (activos.isEmpty()) {
                Text("No hay activos registrados.", modifier = Modifier.padding(16.dp))
            } else if (modoVista == "lista") {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                    items(activos) { activo ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Código: ${activo.codigoInventario}", style = MaterialTheme.typography.titleMedium)
                                Text("Nombre: ${activo.nombre}")
                                Text("Ubicación: ${activo.ubicacion}")
                                Text("Estado: ${activo.estadoFisico}")
                            }
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize().padding(8.dp)
                ) {
                    items(activos) { activo ->
                        Card(modifier = Modifier.padding(4.dp)) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(activo.codigoInventario, style = MaterialTheme.typography.titleSmall)
                                Text(activo.nombre, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}