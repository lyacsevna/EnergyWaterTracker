package com.vstu.energywatertracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vstu.energywatertracker.data.local.entity.MeterReading
import com.vstu.energywatertracker.data.local.entity.MeterType
import com.vstu.energywatertracker.data.repository.MeterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MeterViewModel @Inject constructor(
    private val repository: MeterRepository
) : ViewModel() {

    private val _readings = MutableStateFlow<List<MeterReading>>(emptyList())
    val readings: StateFlow<List<MeterReading>> = _readings.asStateFlow()

    private val _selectedType = MutableStateFlow(MeterType.ELECTRICITY)
    val selectedType: StateFlow<MeterType> = _selectedType.asStateFlow()

    private val _uiState = MutableStateFlow<MeterUiState>(MeterUiState.Loading)
    val uiState: StateFlow<MeterUiState> = _uiState.asStateFlow()

    init {
        loadAllReadings()
    }

    fun loadAllReadings() {
        viewModelScope.launch {
            repository.getAllReadings().collectLatest { readings ->
                _readings.value = readings
                _uiState.value = MeterUiState.Success
            }
        }
    }

    fun loadReadingsByType(type: MeterType) {
        viewModelScope.launch {
            _selectedType.value = type
            repository.getReadingsByType(type).collectLatest { readings ->
                _readings.value = readings
            }
        }
    }

    fun addReading(reading: MeterReading) {
        viewModelScope.launch {
            repository.insertReading(reading)
        }
    }

    fun updateReading(reading: MeterReading) {
        viewModelScope.launch {
            repository.updateReading(reading)
        }
    }

    fun deleteReading(reading: MeterReading) {
        viewModelScope.launch {
            repository.deleteReading(reading)
        }
    }

    fun getLatestReading(type: MeterType): MeterReading? {
        var result: MeterReading? = null
        viewModelScope.launch {
            result = repository.getLatestReadingByType(type)
        }
        return result
    }
}

sealed class MeterUiState {
    data object Loading : MeterUiState()
    data object Success : MeterUiState()
    data class Error(val message: String) : MeterUiState()
}