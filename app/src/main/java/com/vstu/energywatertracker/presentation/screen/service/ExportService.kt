package com.vstu.energywatertracker.domain.usecase

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.vstu.energywatertracker.data.local.entity.MeterReading
import com.vstu.energywatertracker.data.local.entity.MeterType
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class ExportService(private val context: Context) {

    fun exportToCsv(readings: List<MeterReading>): Uri? {
        return try {
            // Создаем временный файл
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "meter_readings_$timeStamp.csv"
            val file = File(context.cacheDir, fileName)

            // Записываем данные в CSV
            FileWriter(file).use { writer ->
                // Заголовок
                writer.write("Тип;Показание;Дата;Заметки\n")

                // Данные
                readings.forEach { reading ->
                    val dateStr = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                        .format(reading.date)
                    val typeStr = when (reading.type) {
                        MeterType.ELECTRICITY -> "Электричество"
                        MeterType.WATER_COLD -> "Холодная вода"
                        MeterType.WATER_HOT -> "Горячая вода"
                    }
                    val notes = reading.notes ?: ""

                    writer.write("$typeStr;${reading.value};$dateStr;$notes\n")
                }
            }

            // Получаем URI через FileProvider
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}