package com.example.proyect2.vistas


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import com.example.proyect2.datos.ConfiguracionRemota
import com.example.proyect2.datos.Tarea
import com.example.proyect2.datos.TareaDBHelper
import com.example.proyect2.datos.sincronizarTareas
import com.example.proyect2.preferencias.AppColors
import com.example.proyect2.preferencias.ConfiguracionApp
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewTareas(
    navController: NavController,
    principalColorState: MutableState<Color>,
    saveColorPreferenceFunc: suspend (Color) -> Unit
) {
    val context = LocalContext.current
    val dbHelper = remember { TareaDBHelper(context) }

    val colores = remember(principalColorState.value) {
        AppColors(
            principal = principalColorState.value,
            claro = principalColorState.value.copy(alpha = 0.3f),
            oscuro = principalColorState.value.darken(0.3f),
            texto = principalColorState.value.darken(0.6f)
        )
    }

    val tareas = remember { mutableStateListOf<Tarea>() }
    var showDialog by remember { mutableStateOf(false) }
    var showRemoteConfigDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var ordenarPorFecha by rememberSaveable { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val scope = rememberCoroutineScope()
    var sincronizando by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        tareas.clear()
        tareas.addAll(dbHelper.obtenerTareas())
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(colores.fondo)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    sincronizando = true
                    mensaje = null
                    scope.launch {
                        try {
                            sincronizarTareas(dbHelper)
                            mensaje = "Sincronización completada"
                        } catch (e: Exception) {
                            mensaje = "Error al sincronizar: ${e.message}"
                        } finally {
                            sincronizando = false
                        }
                        tareas.clear()
                        tareas.addAll(dbHelper.obtenerTareas())
                    }
                },
                enabled = !sincronizando,
                colors = ButtonDefaults.buttonColors(containerColor = colores.principal)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Sync, contentDescription = "Sincronizar", tint = Color.White)
                    Text(" ${if (sincronizando) "Sincronizando..." else "Sincronizar"}", color = Color.White)
                }
            }

            LaunchedEffect(mensaje) {
                mensaje?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }

            Button(
                onClick = { showHelpDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = colores.principal)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Help, contentDescription = "Ayuda", tint = Color.White)
                    Text(" Ayuda", color = Color.White)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = colores.principal)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ColorLens, contentDescription = "Color", tint = Color.White)
                    Text(" Color", color = Color.White)
                }
            }

            Button(
                onClick = {
                    ordenarPorFecha = !ordenarPorFecha
                    coroutineScope.launch {
                        tareas.clear()
                        val nuevasTareas = if (ordenarPorFecha) {
                            dbHelper.obtenerTareasOrdenadasPorFecha()
                        } else {
                            dbHelper.obtenerTareasOrdenadasPorPrioridad()
                        }
                        tareas.addAll(nuevasTareas)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = colores.principal)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (ordenarPorFecha) Icons.Default.List else Icons.Default.DateRange,
                        contentDescription = "Ordenar",
                        tint = Color.White
                    )
                    Text(" ${if (ordenarPorFecha) "Prioridad" else "Fecha"}", color = Color.White)
                }
            }

            Button(
                onClick = { navController.navigate("view_agregarProducto/0") },
                colors = ButtonDefaults.buttonColors(containerColor = colores.principal)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar", tint = Color.White)
                    Text("", color = Color.White)
                }
            }
        }

        LazyColumn {
            items(tareas) { tarea ->
                ProductoItem(tarea, navController, tareas, colores, dbHelper)
            }
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.6f),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = colores.fondo)
            ) {
                val opcionesColor = listOf(
                    "Azul" to "#1976D2",
                    "Verde" to "#388E3C",
                    "Amarillo" to "#FBC02D"
                )

                val opcionesTiempo = listOf(
                    "1 minuto" to 1,
                    "5 minutos" to 5,
                    "10 minutos" to 10,
                    "30 minutos" to 30,
                    "1 hora" to 60
                )

                val tiempoInicial = opcionesTiempo.find { it.second == ConfiguracionApp.tiempoNotificacion }?.first
                    ?: opcionesTiempo.first().first
                var tiempoSeleccionadoTexto by remember { mutableStateOf(tiempoInicial) }

                var expandedTiempo by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Selecciona el color de la app",
                        style = MaterialTheme.typography.headlineMedium,
                        color = colores.texto,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        opcionesColor.forEach { (_, colorHex) ->
                            val color = Color(colorHex.toColorInt())
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .clickable {
                                        principalColorState.value = color
                                        showDialog = false
                                        coroutineScope.launch {
                                            saveColorPreferenceFunc(color)
                                        }
                                    }
                            )
                        }
                    }

                    Text(text = "Tiempo para notificación:", color = colores.texto)

                    Box {
                        Button(
                            onClick = { expandedTiempo = true },
                            colors = ButtonDefaults.buttonColors(containerColor = colores.claro)
                        ) {
                            Text(text = tiempoSeleccionadoTexto, color = colores.texto)
                        }


                        DropdownMenu(
                            expanded = expandedTiempo,
                            onDismissRequest = { expandedTiempo = false }
                        ) {
                            opcionesTiempo.forEach { (texto, valor) ->
                                DropdownMenuItem(
                                    text = { Text(texto) },
                                    onClick = {
                                        tiempoSeleccionadoTexto = texto
                                        expandedTiempo = false
                                        ConfiguracionApp.tiempoNotificacion = valor
                                    }
                                )
                            }
                        }

                    }

                    Button(
                        onClick = { showDialog = false },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(containerColor = colores.principal)
                    ) {
                        Text("Cerrar", color = Color.White)
                    }
                }
            }
        }

    }

    if (showRemoteConfigDialog) {
        Dialog(onDismissRequest = { showRemoteConfigDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = colores.fondo)
            ) {

                var servidor by remember { mutableStateOf(ConfiguracionRemota.servidor) }
                var usuario by remember { mutableStateOf(ConfiguracionRemota.usuario) }
                var contrasena by remember { mutableStateOf(ConfiguracionRemota.contrasena) }
                var baseDatos by remember { mutableStateOf(ConfiguracionRemota.baseDatos) }


                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Configuración de base de datos remota", color = colores.texto)

                    OutlinedTextField(
                        value = servidor,
                        onValueChange = { servidor = it },
                        label = { Text("Servidor") },
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = colores.principal,
                            unfocusedBorderColor = colores.claro,
                            cursorColor = colores.principal,
                            focusedLabelColor = colores.principal,
                            unfocusedLabelColor = colores.texto
                        )
                    )

                    OutlinedTextField(
                        value = usuario,
                        onValueChange = { usuario = it },
                        label = { Text("Usuario") },
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = colores.principal,
                            unfocusedBorderColor = colores.claro,
                            cursorColor = colores.principal,
                            focusedLabelColor = colores.principal,
                            unfocusedLabelColor = colores.texto
                        )
                    )

                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = { contrasena = it },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = colores.principal,
                            unfocusedBorderColor = colores.claro,
                            cursorColor = colores.principal,
                            focusedLabelColor = colores.principal,
                            unfocusedLabelColor = colores.texto
                        )
                    )

                    OutlinedTextField(
                        value = baseDatos,
                        onValueChange = { baseDatos = it },
                        label = { Text("Base de datos") },
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = colores.principal,
                            unfocusedBorderColor = colores.claro,
                            cursorColor = colores.principal,
                            focusedLabelColor = colores.principal,
                            unfocusedLabelColor = colores.texto
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Button(
                            onClick = {
                                ConfiguracionRemota.servidor = servidor
                                ConfiguracionRemota.usuario = usuario
                                ConfiguracionRemota.contrasena = contrasena
                                ConfiguracionRemota.baseDatos = baseDatos
                                showRemoteConfigDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = colores.principal)
                        ) {
                            Text("Guardar", color = Color.White)
                        }


                        Button(
                            onClick = { showRemoteConfigDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = colores.claro)
                        ) {
                            Text("Cancelar", color = colores.texto)
                        }
                    }
                }
            }
        }
    }



    if (showHelpDialog) {
        Dialog(onDismissRequest = { showHelpDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = colores.fondo)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Ayuda de la app",
                        color = colores.texto,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("• Abre la aplicación.", color = colores.texto)
                        Text("• Para agregar una actividad, toca el ícono de \"+\" y completa los detalles.", color = colores.texto)
                        Text("• Para editar una actividad, selecciónala en la lista y realiza los cambios.", color = colores.texto)
                        Text("• Para sincronizar tus actividades, toca el ícono de \"Sincronizar\".", color = colores.texto)
                        Text("• Activa los recordatorios desde la configuración.", color = colores.texto)
                    }

                    Button(
                        onClick = { showHelpDialog = false },
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(containerColor = colores.principal)
                    ) {
                        Text("Cerrar", color = Color.White)
                    }
                }
            }
        }
    }

}


@Composable
fun ProductoItem(
    tarea: Tarea,
    navController: NavController,
    tareas: MutableList<Tarea>,
    colores: AppColors,
    dbHelper: TareaDBHelper
) {
    val contexto = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = colores.claro.copy(alpha = 0.2f))
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(tarea.titulo, style = MaterialTheme.typography.titleMedium, color = colores.principal)

            val fechaFormateada = tarea.fecha?.let {
                try {
                    val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
                    val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm")

                    val fecha = try {
                        LocalDateTime.parse(it, formatter1)
                    } catch (e: DateTimeParseException) {
                        LocalDateTime.parse(it, formatter2)
                    }

                    fecha.format(outputFormatter)
                } catch (e: Exception) {
                    "Formato inválido"
                }
            } ?: "Sin fecha"

            Text("Fecha: $fechaFormateada", style = MaterialTheme.typography.titleMedium, color = colores.texto)
            Text("Prioridad: ${tarea.prioridad}", style = MaterialTheme.typography.bodyMedium, color = colores.texto)

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Button(
                    onClick = { navController.navigate("view_agregarProducto/${tarea.id_tarea}") },
                    colors = ButtonDefaults.buttonColors(containerColor = colores.principal)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.White)
                        Text(" Editar", color = Color.White)
                    }
                }

                Button(
                    onClick = {
                        coroutineScope.launch {
                            dbHelper.eliminarTareaPorIdLocal(tarea.id_tarea)
                            tareas.remove(tarea)
                            Toast.makeText(contexto, "Eliminado", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.White)
                        Text(" Eliminar", color = Color.White)
                    }
                }
            }
        }


    }
}



