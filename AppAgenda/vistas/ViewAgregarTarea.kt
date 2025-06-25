package com.example.proyect2.vistas

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyect2.datos.Tarea
import com.example.proyect2.datos.TareaDBHelper
import com.example.proyect2.datos.obtenerFechaActual
import com.example.proyect2.preferencias.LocalAppColors
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.UUID


fun Color.darken(factor: Float = 0.2f): Color = Color(
    red = (red * (1 - factor)).coerceIn(0f, 1f),
    green = (green * (1 - factor)).coerceIn(0f, 1f),
    blue = (blue * (1 - factor)).coerceIn(0f, 1f),
    alpha = alpha
)

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ViewAgregarTareas(navController: NavController, idP: String?) {
    val contexto = LocalContext.current
    val dbHelper = remember { TareaDBHelper(contexto) }
    val id = idP?.toIntOrNull() ?: 0

    var tarea by remember { mutableStateOf<Tarea?>(null) }

    val coroutineScope = rememberCoroutineScope()

    // Campos editables
    var titulo by rememberSaveable { mutableStateOf("") }
    var prioridad by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var estado by rememberSaveable { mutableStateOf("") }
    var fechaSeleccionada by rememberSaveable { mutableStateOf(LocalDateTime.now()) }
    var idGlobal by rememberSaveable { mutableStateOf<String?>(null) }

    val colores = LocalAppColors.current

    LaunchedEffect(id) {
        if (id != 0) {
            val tareaLocal = dbHelper.obtenerTareaPorId(id)
            tarea = tareaLocal
            tareaLocal?.let {
                titulo = it.titulo
                prioridad = it.prioridad
                descripcion = it.descripcion ?: ""
                estado = it.estado

                val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")

                fechaSeleccionada = it.fecha?.let { fechaStr ->
                    try {
                        LocalDateTime.parse(fechaStr, formatter1)
                    } catch (e1: DateTimeParseException) {
                        try {
                            LocalDateTime.parse(fechaStr, formatter2)
                        } catch (e2: DateTimeParseException) {
                            Log.e("FechaParseo", "Formato no reconocido: $fechaStr")
                            LocalDateTime.now()
                        }
                    }
                } ?: LocalDateTime.now()


                idGlobal = it.idGlobal
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = if (id == 0) "Agregar tarea" else "Editar tarea",
            style = MaterialTheme.typography.headlineSmall,
            color = colores.texto,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título *") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        DateTimeSelector(
            fecha = fechaSeleccionada,
            onFechaHoraCambiada = { fechaSeleccionada = it },
            colorBoton = colores.claro,
            textoColor = colores.texto
        )

        Spacer(modifier = Modifier.height(12.dp))

        DropdownSelector("Prioridad *", prioridad, listOf("Baja", "Media", "Alta"), colores.texto) { prioridad = it }

        Spacer(modifier = Modifier.height(12.dp))

        DropdownSelector("Estado *", estado, listOf("Pendiente", "En progreso", "Completado"), colores.texto) { estado = it }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (titulo.isNotBlank() && prioridad.isNotBlank() && estado.isNotBlank()) {
                    val ahora = LocalDateTime.now()

                    val tareaNueva = Tarea(
                        id_tarea = id,
                        idGlobal = idGlobal ?: UUID.randomUUID().toString(),
                        titulo = titulo,
                        fecha = fechaSeleccionada.toString(),
                        prioridad = prioridad,
                        estado = estado,
                        descripcion = descripcion,
                        notificacion = 0,
                        ultimaModificacion = obtenerFechaActual(),
                        eliminado = 0
                    )

                    coroutineScope.launch {
                        if (id == 0) {
                            dbHelper.insertarTarea(tareaNueva)
                        } else {
                            dbHelper.actualizarTarea(tareaNueva)
                        }

                        Toast.makeText(
                            contexto,
                            if (id == 0) "Tarea guardada" else "Tarea actualizada",
                            Toast.LENGTH_SHORT
                        ).show()

                        navController.popBackStack()
                    }
                } else {
                    Toast.makeText(contexto, "Llena los campos obligatorios", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = colores.principal),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (id == 0) "Guardar" else "Actualizar", color = Color.White)
        }
    }
}


@Composable
fun DateTimeSelector(
    fecha: LocalDateTime,
    onFechaHoraCambiada: (LocalDateTime) -> Unit,
    colorBoton: Color,
    textoColor: Color
) {
    val contexto = LocalContext.current

    val abrirSelectorFecha = {
        DatePickerDialog(
            contexto,
            { _, y, m, d ->
                val nuevaFecha = fecha.withYear(y).withMonth(m + 1).withDayOfMonth(d)
                onFechaHoraCambiada(nuevaFecha)
            },
            fecha.year,
            fecha.monthValue - 1,
            fecha.dayOfMonth
        ).show()
    }

    val abrirSelectorHora = {
        val is24HourFormat = DateFormat.is24HourFormat(contexto)

        TimePickerDialog(
            contexto,
            { _, h, min ->
                val nuevaFecha = fecha.withHour(h).withMinute(min)
                onFechaHoraCambiada(nuevaFecha)
            },
            fecha.hour,
            fecha.minute,
            is24HourFormat
        ).show()
    }

    Text(text = "Fecha y hora *", color = textoColor)
    Spacer(modifier = Modifier.height(4.dp))
    Row(Modifier.fillMaxWidth()) {
        Button(
            onClick = abrirSelectorFecha,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = colorBoton)
        ) {
            Text(fecha.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = abrirSelectorHora,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = colorBoton)
        ) {
            Text(fecha.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")))
        }
    }
}


@Composable
fun DropdownSelector(
    label: String,
    selected: String,
    options: List<String>,
    textoColor: Color,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = label, color = textoColor)
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(disabledTextColor = Color.Black)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}







