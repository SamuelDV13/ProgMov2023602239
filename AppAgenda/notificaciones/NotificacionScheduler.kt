package com.example.proyect2.notificaciones

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.proyect2.datos.Tarea
import com.example.proyect2.preferencias.ConfiguracionApp
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class NotificacionScheduler(private val contexto: Context) {

    // Función para programar la notificación para la tarea
    fun programarNotificacion(tarea: Tarea) {
        Log.d("NotificacionScheduler", "Programando notificación para tarea: ${tarea.titulo}, fecha: ${tarea.fecha}")

        // Verificamos si la fecha de la tarea está presente y es válida
        val fechaNotificacion = try {
            // Convertimos la fecha de String a LocalDateTime

            val tiempo = ConfiguracionApp.tiempoNotificacion
            LocalDateTime.parse(tarea.fecha, DateTimeFormatter.ISO_DATE_TIME).minusMinutes(tiempo.toLong())
        } catch (e: Exception) {
            Log.d("NotificacionScheduler", "Fecha de tarea inválida: ${tarea.fecha}")
            return
        }

        val now = LocalDateTime.now()

        // Calculamos el delay en milisegundos
        val delayMillis = Duration.between(now, fechaNotificacion).toMillis()
        Log.d("NotificacionScheduler", "Delay calculado en ms: $delayMillis")

        // Si la fecha de notificación ya pasó, no programamos la notificación
        if (delayMillis <= 0) {
            Log.d("NotificacionScheduler", "El tiempo ya pasó, no se programa notificación")
            return
        }

        // Creamos los datos de entrada para el Worker
        val datos = workDataOf(
            "titulo" to tarea.titulo,
            "id" to (tarea.id_tarea ?: 0)
        )

        // Creamos la solicitud de trabajo (work request)
        val workRequest = OneTimeWorkRequestBuilder<NotificacionWorker>()
            .setInputData(datos)
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .build()

        // Encolamos la solicitud de trabajo en WorkManager
        WorkManager.getInstance(contexto).enqueue(workRequest)
        Log.d("NotificacionScheduler", "WorkManager encolado correctamente")
    }
}


