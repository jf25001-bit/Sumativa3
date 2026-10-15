package com.example.sumativa3.vistas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sumativa3.viewmodel.InventarioViewModel

@Composable
fun AjustesView(viewModel: InventarioViewModel) {
    val auditorActual by viewModel.codigoAuditor.collectAsState()
    val ubicacionActual by viewModel.ultimaUbicacion.collectAsState()

    var codigoInput by remember(auditorActual) { mutableStateOf(auditorActual) }
    var ubicacionInput by remember(ubicacionActual) { mutableStateOf(ubicacionActual) }
    var guardado by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Perfil / Ajustes del Auditor", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = codigoInput,
            onValueChange = { codigoInput = it },
            label = { Text("Código de Auditor Obligatorio*") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = ubicacionInput,
            onValueChange = { ubicacionInput = it },
            label = { Text("Ubicación Predeterminada") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.guardarAjustes(codigoInput, ubicacionInput)
                guardado = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Preferencias")
        }

        if (guardado) {
            Text("¡Preferencias guardadas!", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 8.dp))
        }
    }
}