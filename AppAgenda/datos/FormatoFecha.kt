package com.example.proyect2.datos

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatearFechaMySQL(fechaMySQL: String): String {
    val formatoEntrada = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val formatoSalida = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
    val fecha = formatoEntrada.parse(fechaMySQL)
    return formatoSalida.format(fecha!!)
}

fun obtenerFechaActual(): String {
    val formato = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return formato.format(Date())
}

fun parseFecha(fecha: String?): Long {
    return try {
        val formato = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        formato.parse(fecha ?: "1970-01-01 00:00:00")?.time ?: 0L
    } catch (e: Exception) {
        0L
    }
}






