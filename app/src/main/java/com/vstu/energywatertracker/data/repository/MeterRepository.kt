package com.vstu.energywatertracker.data.repository

import com.vstu.energywatertracker.data.local.dao.MeterReadingDao
import com.vstu.energywatertracker.data.local.entity.MeterReading
import com.vstu.energywatertracker.data.local.entity.MeterType
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class MeterRepository @Inject constructor(
    private val meterReadingDao: MeterReadingDao
) {

    fun getAllReadings(): Flow<List<MeterReading>> {
        return meterReadingDao.getAllReadings()
    }

    fun getReadingsByType(type: MeterType): Flow<List<MeterReading>> {
        return meterReadingDao.getReadingsByType(type)
    }

    suspend fun insertReading(reading: MeterReading): Long {
        return meterReadingDao.insert(reading)
    }

    suspend fun updateReading(reading: MeterReading) {
        meterReadingDao.update(reading)
    }

    suspend fun deleteReading(reading: MeterReading) {
        meterReadingDao.delete(reading)
    }

    suspend fun getLatestReadingByType(type: MeterType): MeterReading? {
        return meterReadingDao.getLatestReadingByType(type)
    }

    fun getReadingsBetweenDates(
        startDate: Date,
        endDate: Date,
        type: MeterType
    ): Flow<List<MeterReading>> {
        return meterReadingDao.getReadingsBetweenDates(startDate, endDate, type)
    }
}