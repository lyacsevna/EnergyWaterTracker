package com.vstu.energywatertracker.data.local.dao

import androidx.room.*
import com.vstu.energywatertracker.data.local.entity.MeterReading
import com.vstu.energywatertracker.data.local.entity.MeterType
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface MeterReadingDao {

    @Insert
    suspend fun insert(reading: MeterReading): Long

    @Update
    suspend fun update(reading: MeterReading)

    @Delete
    suspend fun delete(reading: MeterReading)

    @Query("SELECT * FROM meter_readings ORDER BY date DESC")
    fun getAllReadings(): Flow<List<MeterReading>>

    @Query("SELECT * FROM meter_readings WHERE type = :type ORDER BY date DESC")
    fun getReadingsByType(type: MeterType): Flow<List<MeterReading>>

    @Query("SELECT * FROM meter_readings WHERE date BETWEEN :startDate AND :endDate AND type = :type")
    fun getReadingsBetweenDates(
        startDate: Date,
        endDate: Date,
        type: MeterType
    ): Flow<List<MeterReading>>

    @Query("SELECT * FROM meter_readings WHERE id = :id")
    suspend fun getReadingById(id: Long): MeterReading?

    @Query("SELECT * FROM meter_readings WHERE type = :type ORDER BY date DESC LIMIT 1")
    suspend fun getLatestReadingByType(type: MeterType): MeterReading?
}