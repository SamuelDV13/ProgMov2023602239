package com.example.proyect2.notificaciones

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.proyect2.R  // IMPORTANTE: importa tu R local, no androidx.work.R

class NotificacionWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val titulo = inputData.getString("titulo") ?: "Recordatorio"
        val id = inputData.getInt("id", 0)

        Log.d("NotificacionWorker", "Ejecutando Worker para tarea: $titulo, id: $id")

        val intent = applicationContext.packageManager.getLaunchIntentForPackage(applicationContext.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                "canal_tareas_id",
                "Canal Tareas",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(canal)
        }

        val notification = NotificationCompat.Builder(applicationContext, "canal_tareas_id")
            .setContentTitle(titulo)
            .setSmallIcon(R.drawable.ic_launcher_background)  // Tu ícono aquí
            .setContentText("Falta 1 minuto para la tarea")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(id, notification)

        return Result.success()
    }
}


