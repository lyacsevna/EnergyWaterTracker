package com.vstu.energywatertracker.presentation.screen.main


import kotlin.math.roundToInt


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.vstu.energywatertracker.data.local.entity.MeterReading
import com.vstu.energywatertracker.data.local.entity.MeterType
import com.vstu.energywatertracker.presentation.viewmodel.MeterViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MainScreen(viewModel: MeterViewModel) {
    val readings by viewModel.readings.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var readingToDelete by remember { mutableStateOf<MeterReading?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Приветствие
        Text(
            text = "Учет потребления",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Добро пожаловать!",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Быстрые действия
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickActionCard(
                title = "Ввести показания",
                description = "Электричество",
                icon = Icons.Default.Bolt,
                onClick = { /* Навигация к вводу */ },
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                title = "Ввести показания",
                description = "Вода",
                icon = Icons.Default.WaterDrop,
                onClick = { /* Навигация к вводу */ },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Последние показания
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Последние показания",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Всего: ${readings.size}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (readings.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Нет данных")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Добавьте первое показание",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(readings.take(10)) { reading ->
                    ReadingCard(
                        reading = reading,
                        onDelete = {
                            readingToDelete = reading
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }

        // Статистика
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Краткая статистика",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Рассчитаем реальную статистику
            val electricityReadings = readings.filter { it.type == MeterType.ELECTRICITY }
            val waterReadings = readings.filter { it.type in listOf(MeterType.WATER_COLD, MeterType.WATER_HOT) }

            val electricityTotal = electricityReadings.sumOf { it.value }
            val waterTotal = waterReadings.sumOf { it.value }

            StatCard(
                title = "Электроэнергия",
                value = "${electricityTotal.roundToInt()} кВт",
                change = if (electricityReadings.size >= 2) {
                    val last = electricityReadings.takeLast(2)
                    if (last.size == 2) {
                        val diff = ((last[1].value - last[0].value) / last[0].value * 100).roundToInt()
                        if (diff >= 0) "+$diff%" else "$diff%"
                    } else "0%"
                } else "0%",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Вода",
                value = "${waterTotal.roundToInt()} м³",
                change = if (waterReadings.size >= 2) {
                    val last = waterReadings.takeLast(2)
                    if (last.size == 2) {
                        val diff = ((last[1].value - last[0].value) / last[0].value * 100).roundToInt()
                        if (diff >= 0) "+$diff%" else "$diff%"
                    } else "0%"
                } else "0%",
                modifier = Modifier.weight(1f)
            )
        }

        // Диалог подтверждения удаления
        if (showDeleteDialog && readingToDelete != null) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                    readingToDelete = null
                },
                title = { Text("Удалить запись?") },
                text = {
                    val reading = readingToDelete!!
                    Column {
                        Text("Тип: ${getMeterTypeDisplayName(reading.type)}")
                        Text("Значение: ${reading.value} ${getUnit(reading.type)}")
                        Text("Дата: ${SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(reading.date)}")
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            readingToDelete?.let { viewModel.deleteReading(it) }
                            showDeleteDialog = false
                            readingToDelete = null
                        }
                    ) {
                        Text("Удалить", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            readingToDelete = null
                        }
                    ) {
                        Text("Отмена")
                    }
                }
            )
        }
    }
}

@Composable
fun ReadingCard(
    reading: MeterReading,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = getMeterTypeDisplayName(reading.type),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(reading.date),
                    style = MaterialTheme.typography.bodySmall
                )
                reading.notes?.let { notes ->
                    if (notes.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = notes,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${reading.value} ${getUnit(reading.type)}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickActionCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun ReadingCard(reading: MeterReading) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = getMeterTypeDisplayName(reading.type),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                        .format(Date(reading.date)),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = "${reading.value} ${getUnit(reading.type)}",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    change: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = change,
                style = MaterialTheme.typography.bodySmall,
                color = if (change.startsWith("+")) Color.Green else Color.Red
            )
        }
    }
}

private fun getUnit(type: MeterType): String {
    return when (type) {
        MeterType.ELECTRICITY -> "кВт"
        MeterType.WATER_COLD -> "м³"
        MeterType.WATER_HOT -> "м³"
    }
}

private fun getMeterTypeDisplayName(type: MeterType): String {
    return when (type) {
        MeterType.ELECTRICITY -> "Электричество"
        MeterType.WATER_COLD -> "Холодная вода"
        MeterType.WATER_HOT -> "Горячая вода"
    }
}