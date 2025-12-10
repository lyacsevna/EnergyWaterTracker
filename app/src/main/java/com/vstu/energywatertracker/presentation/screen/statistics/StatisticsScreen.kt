package com.vstu.energywatertracker.presentation.screen.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vstu.energywatertracker.data.local.entity.MeterType
import com.vstu.energywatertracker.presentation.viewmodel.MeterViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(viewModel: MeterViewModel) {
    var selectedType by remember { mutableStateOf(MeterType.ELECTRICITY) }
    var selectedPeriod by remember { mutableStateOf(Period.LAST_6_MONTHS) }
    var chartType by remember { mutableStateOf(ChartType.BAR) }

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
            Period.values().forEach { period ->
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
            ChartType.values().forEach { type ->
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
                data = sampleData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
            ChartType.PIE -> PieChart(
                data = sampleData,
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
                StatItem("Среднее потребление", "120 кВт/ч")
                StatItem("Максимальное", "150 кВт/ч")
                StatItem("Минимальное", "90 кВт/ч")
                StatItem("Общее за период", "720 кВт/ч")
            }
        }
    }
}

@Composable
fun BarChart(data: List<Pair<String, Double>>, modifier: Modifier = Modifier) {
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
                            .background(Color.Blue)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = value.roundToInt().toString(),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
fun PieChart(data: List<Pair<String, Double>>, modifier: Modifier = Modifier) {
    // Упрощенная круговая диаграмма
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text("Круговая диаграмма\n(будет реализована с библиотекой)")
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
    Spacer(modifier = Modifier.height(4.dp))
}

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

// Пример данных
private val sampleData = listOf(
    "Янв" to 120.0,
    "Фев" to 135.0,
    "Мар" to 110.0,
    "Апр" to 145.0,
    "Май" to 130.0,
    "Июн" to 125.0
)