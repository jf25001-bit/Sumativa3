package com.example.sumativa3.vistas

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.sumativa3.data.ActivoFijo
import com.example.sumativa3.viewmodel.InventarioViewModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RegistrarActivoView(viewModel: InventarioViewModel) {
    val context = LocalContext.current

    val auditorPorDefecto by viewModel.codigoAuditor.collectAsState()
    val ultimaUbicacion by viewModel.ultimaUbicacion.collectAsState()

    var codigoInventario by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Equipo Informático") }
    var ubicacion by remember { mutableStateOf("") }
    var estadoFisico by remember { mutableStateOf("Bueno") }
    var fotoUri by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf<String?>(null) }
    var mensajeExito by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(ultimaUbicacion) {
        if (ubicacion.isEmpty()) ubicacion = ultimaUbicacion
    }

    // 1. Launcher de la cámara: recibe la foto y la guarda como archivo real
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            val archivo = File(context.filesDir, "activo_${System.currentTimeMillis()}.jpg")
            FileOutputStream(archivo).use { salida ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, salida)
            }
            fotoUri = archivo.absolutePath
        }
    }

    // 2. Launcher del permiso: si lo conceden, abre la cámara
    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(
                context,
                "Se necesita el permiso de cámara para tomar la fotografía",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Registrar Activo Fijo", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(value = codigoInventario, onValueChange = { codigoInventario = it }, label = { Text("Código de Inventario*") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre del Equipo*") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría*") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = ubicacion, onValueChange = { ubicacion = it }, label = { Text("Ubicación*") }, modifier = Modifier.fillMaxWidth())

        Text("Estado Físico:", modifier = Modifier.padding(top = 8.dp))
        Row {
            listOf("Bueno", "Regular", "Dañado").forEach { estado ->
                Row(modifier = Modifier.padding(end = 8.dp)) {
                    RadioButton(selected = estadoFisico == estado, onClick = { estadoFisico = estado })
                    Text(estado, modifier = Modifier.padding(start = 4.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 3. El botón revisa el permiso ANTES de abrir la cámara
        Button(onClick = {
            val tienePermiso = ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED

            if (tienePermiso) {
                cameraLauncher.launch(null)
            } else {
                permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
            }
        }) {
            Text("Tomar Fotografía")
        }

        if (fotoUri.isNotEmpty()) {
            Text("Foto lista", color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (codigoInventario.isBlank() || nombre.isBlank() || ubicacion.isBlank() || auditorPorDefecto.isBlank()) {
                    mensajeError = "Complete los campos obligatorios y configure su código de auditor en Ajustes."
                    mensajeExito = null
                } else {
                    val fechaActual = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                    val nuevoActivo = ActivoFijo(
                        codigoInventario = codigoInventario,
                        nombre = nombre,
                        categoria = categoria,
                        ubicacion = ubicacion,
                        estadoFisico = estadoFisico,
                        fechaAdquisition = fechaActual,
                        fotoUri = fotoUri,
                        codigoAuditor = auditorPorDefecto,
                        fechaRegistro = fechaActual
                    )

                    viewModel.registrarActivo(nuevoActivo) { exito, error ->
                        if (exito) {
                            mensajeExito = "¡Activo registrado con éxito!"
                            mensajeError = null
                            codigoInventario = ""
                            nombre = ""
                            fotoUri = ""
                        } else {
                            mensajeError = error
                            mensajeExito = null
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Activo")
        }

        mensajeError?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp)) }
        mensajeExito?.let { Text(it, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 8.dp)) }
    }
}