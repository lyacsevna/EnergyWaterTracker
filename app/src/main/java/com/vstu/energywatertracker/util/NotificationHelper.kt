package com.vstu.energywatertracker.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.*
import java.util.concurrent.TimeUnit

object NotificationHelper {

    private const val CHANNEL_ID = "meter_reminder_channel"
    private const val CHANNEL_NAME = "Напоминания о показаниях"
    private const val WORK_TAG = "meter_reminder_work"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Напоминания о необходимости передачи показаний счетчиков"
            }

            val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleMonthlyReminder(context: Context, dayOfMonth: Int) {
        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(true)
            .build()

        // Планируем на указанный день месяца в 10:00
        val currentTime = System.currentTimeMillis()
        val calendar = java.util.Calendar.getInstance().apply {
            timeInMillis = currentTime
            set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth)
            set(java.util.Calendar.HOUR_OF_DAY, 10)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)

            // Если время уже прошло сегодня, планируем на следующий месяц
            if (timeInMillis <= currentTime) {
                add(java.util.Calendar.MONTH, 1)
            }
        }

        val initialDelay = calendar.timeInMillis - currentTime

        val periodicWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            30, TimeUnit.DAYS // Повторяем каждые 30 дней
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .addTag(WORK_TAG)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_TAG,
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )
    }

    fun cancelReminders(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG)
    }
}