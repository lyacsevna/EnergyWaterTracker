package com.vstu.energywatertracker.data.local.converters

import androidx.room.TypeConverter
import com.vstu.energywatertracker.data.local.entity.MeterType

class MeterTypeConverter {
    @TypeConverter
    fun fromString(value: String): MeterType {
        return MeterType.valueOf(value)
    }

    @TypeConverter
    fun toString(type: MeterType): String {
        return type.name
    }
}