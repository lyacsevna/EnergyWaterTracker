package com.vstu.energywatertracker.util

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        // Пока просто возвращаем успех
        // Реализуем позже
        return Result.success()
    }
}