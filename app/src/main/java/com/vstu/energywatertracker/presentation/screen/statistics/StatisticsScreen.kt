package com.vstu.energywatertracker.presentation.screen.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vstu.energywatertracker.data.local.entity.MeterReading
import com.vstu.energywatertracker.data.local.entity.MeterType
import com.vstu.energywatertracker.presentation.viewmodel.MeterViewModel
import kotlin.math.roundToInt
import java.util.*
import kotlin.math.max
import kotlin.math.min

@Composable
fun StatisticsScreen(viewModel: MeterViewModel) {
    var selectedType by remember { mutableStateOf(MeterType.ELECTRICITY) }
    var selectedPeriod by remember { mutableStateOf(Period.LAST_6_MONTHS) }
    var chartType by remember { mutableStateOf(ChartType.BAR) }

    val readings by viewModel.readings.collectAsState()

    // Фильтруем показания по выбранному типу
    val filteredReadings = remember(readings, selectedType) {
        readings.filter { it.type == selectedType }
    }

    // Рассчитываем статистику
    val stats = remember(filteredReadings) {
        calculateStatistics(filteredReadings)
    }

    // Готовим данные для диаграммы
    val chartData = remember(filteredReadings, selectedPeriod) {
        prepareChartData(filteredReadings, selectedPeriod)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Фильтры
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedType == MeterType.ELECTRICITY,
                onClick = { selectedType = MeterType.ELECTRICITY },
                label = { Text("Электричество") }
            )
            FilterChip(
                selected = selectedType == MeterType.WATER_COLD,
                onClick = { selectedType = MeterType.WATER_COLD },
                label = { Text("Холодная вода") }
            )
            FilterChip(
                selected = selectedType == MeterType.WATER_HOT,
                onClick = { selectedType = MeterType.WATER_HOT },
                label = { Text("Горячая вода") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Период
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Period.entries.forEach { period ->
                FilterChip(
                    selected = selectedPeriod == period,
                    onClick = { selectedPeriod = period },
                    label = { Text(period.displayName) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Тип диаграммы
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ChartType.entries.forEach { type ->
                FilterChip(
                    selected = chartType == type,
                    onClick = { chartType = type },
                    label = { Text(type.displayName) }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Диаграмма
        when (chartType) {
            ChartType.BAR -> BarChart(
                data = chartData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
            ChartType.PIE -> PieChart(
                data = preparePieChartData(readings),
                modifier = Modifier
                    .size(250.dp)
            )
        }

        // Статистика
        Spacer(modifier = Modifier.height(32.dp))
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Статистика",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (filteredReadings.isNotEmpty()) {
                    StatItem("Всего записей", "${filteredReadings.size}")
                    StatItem("Среднее", "${stats.average.roundToInt()} ${getUnit(selectedType)}")
                    StatItem("Максимальное", "${stats.max.roundToInt()} ${getUnit(selectedType)}")
                    StatItem("Минимальное", "${stats.min.roundToInt()} ${getUnit(selectedType)}")
                    StatItem("Общее", "${stats.total.roundToInt()} ${getUnit(selectedType)}")
                } else {
                    Text("Нет данных для отображения")
                }
            }
        }
    }
}

@Composable
fun BarChart(data: List<Pair<String, Double>>, modifier: Modifier = Modifier) {
    if (data.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text("Нет данных для диаграммы")
        }
        return
    }

    val maxValue = data.maxOfOrNull { it.second } ?: 0.0

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            data.forEach { (label, value) ->
                val heightPercentage = (value / maxValue).coerceIn(0.0, 1.0)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height((heightPercentage * 200).dp)
                            .background(
                                when (label) {
                                    "Янв", "Фев", "Мар" -> Color.Blue
                                    "Апр", "Май", "Июн" -> Color.Green
                                    "Июл", "Авг", "Сен" -> Color.Yellow
                                    else -> Color.Red
                                }
                            )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = value.toInt().toString(),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
fun PieChart(data: List<Pair<String, Double>>, modifier: Modifier = Modifier) {
    if (data.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text("Нет данных для диаграммы")
        }
        return
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Распределение потребления")
            Spacer(modifier = Modifier.height(8.dp))
            data.forEach { (label, value) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("$label:")
                    Text("${value.roundToInt()}%")
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
    Spacer(modifier = Modifier.height(4.dp))
}

private fun getUnit(type: MeterType): String {
    return when (type) {
        MeterType.ELECTRICITY -> "кВт"
        MeterType.WATER_COLD -> "м³"
        MeterType.WATER_HOT -> "м³"
    }
}

private fun calculateStatistics(readings: List<MeterReading>): Statistics {
    if (readings.isEmpty()) return Statistics()

    val values = readings.map { it.value }
    return Statistics(
        average = values.average(),
        max = values.max(),
        min = values.min(),
        total = values.sum()
    )
}

private fun prepareChartData(readings: List<MeterReading>, period: Period): List<Pair<String, Double>> {
    if (readings.isEmpty()) return emptyList()

    val calendar = Calendar.getInstance()
    val months = when (period) {
        Period.LAST_MONTH -> 1
        Period.LAST_3_MONTHS -> 3
        Period.LAST_6_MONTHS -> 6
        Period.LAST_YEAR -> 12
    }

    val monthNames = listOf("Янв", "Фев", "Мар", "Апр", "Май", "Июн",
        "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек")

    return List(months) { index ->
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.MONTH, -index)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val monthReadings = readings.filter { reading ->
            val readingCalendar = Calendar.getInstance().apply {
                timeInMillis = reading.date  // Используем Long напрямую
            }
            readingCalendar.get(Calendar.MONTH) == month &&
                    readingCalendar.get(Calendar.YEAR) == year
        }

        val total = monthReadings.sumOf { it.value }
        Pair(monthNames[month], total)
    }.reversed()
}

private fun preparePieChartData(readings: List<MeterReading>): List<Pair<String, Double>> {
    if (readings.isEmpty()) return emptyList()

    val total = readings.sumOf { it.value }
    if (total == 0.0) return emptyList()

    return MeterType.entries.map { type ->
        val typeReadings = readings.filter { it.type == type }
        val typeTotal = typeReadings.sumOf { it.value }
        val percentage = (typeTotal / total) * 100
        Pair(
            when (type) {
                MeterType.ELECTRICITY -> "Электричество"
                MeterType.WATER_COLD -> "Холодная вода"
                MeterType.WATER_HOT -> "Горячая вода"
            },
            percentage
        )
    }.filter { it.second > 0 }
}

data class Statistics(
    val average: Double = 0.0,
    val max: Double = 0.0,
    val min: Double = 0.0,
    val total: Double = 0.0
)

enum class Period(val displayName: String) {
    LAST_MONTH("Месяц"),
    LAST_3_MONTHS("Квартал"),
    LAST_6_MONTHS("6 месяцев"),
    LAST_YEAR("Год")
}

enum class ChartType(val displayName: String) {
    BAR("Столбчатая"),
    PIE("Круговая")
}