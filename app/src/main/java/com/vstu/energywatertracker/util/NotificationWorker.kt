package com.vstu.energywatertracker.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.vstu.energywatertracker.R
import java.util.*

class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        // Сначала создаем канал уведомлений
        createNotificationChannel()

        // Проверяем разрешение перед отправкой
        if (hasNotificationPermission()) {
            sendReminderNotification()
        }
        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Напоминания о необходимости передачи показаний счетчиков"
            }

            val notificationManager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun hasNotificationPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return NotificationManagerCompat.from(applicationContext)
                .areNotificationsEnabled()
        }
        return true // Для версий ниже Android 13 разрешение не требуется
    }

    private fun sendReminderNotification() {
        val notificationId = Date().time.toInt()

        val notification = NotificationCompat.Builder(
            applicationContext,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Используйте существующую иконку
            .setContentTitle("Пора передать показания!")
            .setContentText("Не забудьте передать показания счетчиков за этот месяц")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(applicationContext)
            .notify(notificationId, notification)
    }

    companion object {
        const val CHANNEL_ID = "meter_reminder_channel"
        const val CHANNEL_NAME = "Напоминания о показаниях"
    }
}