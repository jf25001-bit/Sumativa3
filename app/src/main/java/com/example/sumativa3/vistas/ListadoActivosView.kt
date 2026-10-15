package com.example.sumativa3.vistas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.sumativa3.data.ActivoFijo
import com.example.sumativa3.viewmodel.InventarioViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun ListadoActivosView(viewModel: InventarioViewModel) {
    val activos by viewModel.listaActivos.collectAsState()
    val modoVista by viewModel.modoVista.collectAsState()
    val esCuadricula = modoVista == "cuadricula"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Encabezado: contador + botón para cambiar lista/cuadrícula
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Activos Registrados: ${activos.size}",
                style = MaterialTheme.typography.titleLarge
            )
            IconButton(onClick = {
                viewModel.cambiarModoVista(if (esCuadricula) "lista" else "cuadricula")
            }) {
                Icon(
                    imageVector = if (esCuadricula) Icons.AutoMirrored.Filled.ViewList else Icons.Default.GridView,
                    contentDescription = "Cambiar vista"
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (activos.isEmpty()) {
            Text("Aún no hay activos registrados.")
        } else if (esCuadricula) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(activos) { activo -> TarjetaActivo(activo) }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(activos) { activo -> TarjetaActivo(activo) }
            }
        }
    }
}

@Composable
fun TarjetaActivo(activo: ActivoFijo) {
    val context = LocalContext.current
    //se carga la imagen agregara en
    val imageBitmap by produceState<ImageBitmap?>(initialValue = null, activo.fotoUri) {
        value = withContext(Dispatchers.IO) {
            cargarBitmap(context, activo.fotoUri)?.asImageBitmap()
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            val foto = imageBitmap
            if (foto != null) {
                Image(
                    bitmap = foto,
                    contentDescription = "Foto del activo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text("Código: ${activo.codigoInventario}", style = MaterialTheme.typography.bodyLarge)
            Text("Nombre: ${activo.nombre}", style = MaterialTheme.typography.bodyLarge)
            Text("Ubicación: ${activo.ubicacion}", style = MaterialTheme.typography.bodyLarge)
            Text("Estado: ${activo.estadoFisico}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

private fun cargarBitmap(context: Context, ruta: String): Bitmap? {
    if (ruta.isBlank()) return null
    return try {
        val opciones = BitmapFactory.Options().apply { inSampleSize = 2 }
        val archivo = File(ruta)
        if (archivo.exists()) {
            BitmapFactory.decodeFile(archivo.absolutePath, opciones)
        } else {
            val uri = Uri.parse(ruta)
            context.contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream, null, opciones)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}