package com.vstu.energywatertracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "meter_readings")
data class MeterReading(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: MeterType,
    val value: Double,
    val date: Date,
    val photoPath: String? = null,
    val notes: String? = null
)

enum class MeterType {
    ELECTRICITY,
    WATER_COLD,
    WATER_HOT
}