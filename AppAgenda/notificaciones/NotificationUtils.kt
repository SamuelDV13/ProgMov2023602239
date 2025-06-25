package com.example.proyect2.notificaciones

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.proyect2.datos.Tarea
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// Función para crear un canal de notificaciones
fun crearCanalNotificacion(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Recordatorios de tareas"
        val descriptionText = "Notificaciones para tareas pendientes"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("tareas_channel", name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

@SuppressLint("ScheduleExactAlarm")
fun programarNotificacion(context: Context, tarea: Tarea) {
    // Verificar si la fecha es válida
    val fechaNotificacion = try {
        // Convertir la fecha de String a LocalDateTime
        LocalDateTime.parse(tarea.fecha, DateTimeFormatter.ISO_DATE_TIME).minusMinutes(1)
    } catch (e: Exception) {
        // Si no se puede convertir, no se programa la notificación
        return
    }

    // Convertir LocalDateTime a milisegundos
    val timeInMillis = fechaNotificacion.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    val intent = Intent(context, TareaNotificationReceiver::class.java).apply {
        putExtra("titulo", tarea.titulo)
        putExtra("id", tarea.id_tarea ?: 0)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        tarea.id_tarea ?: 0,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
}

