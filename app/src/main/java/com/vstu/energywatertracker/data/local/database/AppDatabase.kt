package com.vstu.energywatertracker.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vstu.energywatertracker.data.local.dao.MeterReadingDao
import com.vstu.energywatertracker.data.local.entity.MeterReading

@Database(
    entities = [MeterReading::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun meterReadingDao(): MeterReadingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "energy_water_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}