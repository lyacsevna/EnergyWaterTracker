package com.vstu.energywatertracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.vstu.energywatertracker.data.local.entity.MeterReading
import com.vstu.energywatertracker.data.local.entity.MeterType
import java.util.Date

@HiltViewModel
class MeterViewModel @Inject constructor() : ViewModel() {

    private val _readings = MutableStateFlow<List<MeterReading>>(emptyList())
    val readings: StateFlow<List<MeterReading>> = _readings.asStateFlow()

    private val _selectedType = MutableStateFlow(MeterType.ELECTRICITY)
    val selectedType: StateFlow<MeterType> = _selectedType.asStateFlow()

    init {
        // Тестовые данные
        val testReadings = listOf(
            MeterReading(
                type = MeterType.ELECTRICITY,
                value = 1500.0,
                date = Date(System.currentTimeMillis() - 86400000L * 30),
                notes = "Начальные показания"
            ),
            MeterReading(
                type = MeterType.ELECTRICITY,
                value = 1550.0,
                date = Date(),
                notes = "Текущие показания"
            ),
            MeterReading(
                type = MeterType.WATER_COLD,
                value = 50.0,
                date = Date(System.currentTimeMillis() - 86400000L * 15),
                notes = "Холодная вода"
            ),
            MeterReading(
                type = MeterType.WATER_HOT,
                value = 30.0,
                date = Date(),
                notes = "Горячая вода"
            )
        )
        _readings.value = testReadings
    }

    fun addReading(reading: MeterReading) {
        viewModelScope.launch {
            val current = _readings.value.toMutableList()
            current.add(reading)
            _readings.value = current
        }
    }

    fun deleteReading(reading: MeterReading) {
        viewModelScope.launch {
            val current = _readings.value.toMutableList()
            current.remove(reading)
            _readings.value = current
        }
    }

    fun getLatestReading(type: MeterType): MeterReading? {
        return _readings.value
            .filter { it.type == type }
            .maxByOrNull { it.date.time }
    }

    fun setSelectedType(type: MeterType) {
        _selectedType.value = type
    }
}