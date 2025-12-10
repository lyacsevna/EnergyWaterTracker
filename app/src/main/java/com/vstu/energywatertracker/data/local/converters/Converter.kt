package com.vstu.energywatertracker.data.local.converters

import androidx.room.TypeConverter
import com.vstu.energywatertracker.data.local.entity.MeterType
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromMeterType(value: String): MeterType {
        return MeterType.valueOf(value)
    }

    @TypeConverter
    fun meterTypeToString(type: MeterType): String {
        return type.name
    }
}