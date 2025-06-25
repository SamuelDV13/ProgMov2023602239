package com.example.proyect2.datos

import kotlinx.serialization.Serializable

@Serializable
data class Tarea(
    val id_tarea: Int = 0,
    val idGlobal: String? = null,
    val titulo: String = "",
    val fecha: String? = null,
    val prioridad: String = "",
    val estado: String = "",
    val descripcion: String? = null,
    val notificacion: Int? = null,
    val ultimaModificacion: String? = null,
    val eliminado: Int = 0
)
