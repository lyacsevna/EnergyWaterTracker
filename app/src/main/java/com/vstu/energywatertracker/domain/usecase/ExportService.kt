package com.vstu.energywatertracker.domain.usecase

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.vstu.energywatertracker.data.local.entity.MeterReading
import com.vstu.energywatertracker.data.local.entity.MeterType
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExportService(private val context: Context) {

    fun exportToCsv(readings: List<MeterReading>): Uri? {
        return try {
            // Создаем временный файл
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "meter_readings_$timeStamp.csv"
            val file = File(context.cacheDir, fileName)

            // Записываем данные в CSV с правильным разделителем
            FileWriter(file).use { writer ->
                // Заголовок с учетом локали (в русской локали лучше использовать ;)
                writer.write("Тип счетчика;Показание;Дата;Время;Заметки\n")

                // Данные
                readings.sortedByDescending { it.date }.forEach { reading ->
                    val date = Date(reading.date)
                    val dateStr = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date)
                    val timeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)

                    val typeStr = when (reading.type) {
                        MeterType.ELECTRICITY -> "Электричество"
                        MeterType.WATER_COLD -> "Холодная вода"
                        MeterType.WATER_HOT -> "Горячая вода"
                    }

                    val notes = reading.notes ?: ""
                    val valueStr = String.Companion.format(Locale.getDefault(), "%.2f", reading.value)

                    writer.write("$typeStr;$valueStr;$dateStr;$timeStr;$notes\n")
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

    fun exportToJson(readings: List<MeterReading>): Uri? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "meter_readings_$timeStamp.json"
            val file = File(context.cacheDir, fileName)

            FileWriter(file).use { writer ->
                writer.write("{\n")
                writer.write("  \"readings\": [\n")

                readings.sortedByDescending { it.date }.forEachIndexed { index, reading ->
                    val date = Date(reading.date)
                    val dateStr = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(date)

                    val typeStr = when (reading.type) {
                        MeterType.ELECTRICITY -> "electricity"
                        MeterType.WATER_COLD -> "water_cold"
                        MeterType.WATER_HOT -> "water_hot"
                    }

                    writer.write("    {\n")
                    writer.write("      \"id\": ${reading.id},\n")
                    writer.write("      \"type\": \"$typeStr\",\n")
                    writer.write("      \"value\": ${reading.value},\n")
                    writer.write("      \"date\": \"$dateStr\",\n")
                    writer.write("      \"notes\": \"${reading.notes ?: ""}\"\n")
                    writer.write("    }${if (index < readings.size - 1) "," else ""}\n")
                }

                writer.write("  ]\n")
                writer.write("}\n")
            }

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