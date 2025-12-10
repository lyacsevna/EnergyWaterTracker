package com.vstu.energywatertracker.presentation.screen.settings

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.vstu.energywatertracker.presentation.viewmodel.MeterViewModel
import com.vstu.energywatertracker.util.NotificationHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: MeterViewModel) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkThemeEnabled by remember { mutableStateOf(false) }
    var reminderDay by remember { mutableStateOf(25) }
    var exportDialog by remember { mutableStateOf(false) }
    var importDialog by remember { mutableStateOf(false) }
    var clearDataDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Уведомления
        ListItem(
            headlineContent = { Text("Уведомления") },
            leadingContent = {
                Icon(Icons.Default.Notifications, contentDescription = null)
            }
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Напоминания о показаниях")
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { enabled ->
                            notificationsEnabled = enabled
                        }
                    )
                }

                if (notificationsEnabled) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("День месяца для напоминания:")
                    Slider(
                        value = reminderDay.toFloat(),
                        onValueChange = { value ->
                            reminderDay = value.toInt()
                        },
                        valueRange = 1f..31f,
                        steps = 30
                    )
                    Text("$reminderDay число")
                }
            }
        }

        // Внешний вид
        ListItem(
            headlineContent = { Text("Внешний вид") },
            leadingContent = {
                Icon(Icons.Default.Palette, contentDescription = null)
            }
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Темная тема")
                    Switch(
                        checked = darkThemeEnabled,
                        onCheckedChange = { darkThemeEnabled = it }
                    )
                }
            }
        }

        // Данные
        ListItem(
            headlineContent = { Text("Данные") },
            leadingContent = {
                Icon(Icons.Default.Storage, contentDescription = null)
            }
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val readings by viewModel.readings.collectAsState()

                Button(
                    onClick = { exportDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = readings.isNotEmpty()
                ) {
                    Text("Экспорт данных (${readings.size} записей)")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { importDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Импорт данных")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { clearDataDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = readings.isNotEmpty()
                ) {
                    Text("Очистить все данные", color = MaterialTheme.colorScheme.error)
                }
            }
        }

        // О приложении
        ListItem(
            headlineContent = { Text("О приложении") },
            leadingContent = {
                Icon(Icons.Default.Info, contentDescription = null)
            }
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Energy Water Tracker",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Версия 1.0",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "ВГТУ 2025",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Курсовой проект по РПДМУ",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Студент: Ремнева С.А., группа бПО-221",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Руководитель: В.В. Сокольников",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Диалоги
        if (exportDialog) {
            ExportDialog(
                onDismiss = { exportDialog = false },
                onExport = {
                    // TODO: Реализовать экспорт
                    exportDialog = false
                }
            )
        }

        if (clearDataDialog) {
            AlertDialog(
                onDismissRequest = { clearDataDialog = false },
                title = { Text("Очистить все данные?") },
                text = { Text("Это действие нельзя отменить. Все записи будут удалены.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // TODO: Реализовать очистку
                            clearDataDialog = false
                        }
                    ) {
                        Text("Очистить", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { clearDataDialog = false }
                    ) {
                        Text("Отмена")
                    }
                }
            )
        }
    }
}

@Composable
fun ExportDialog(
    onDismiss: () -> Unit,
    onExport: () -> Unit
) {
    var exportFormat by remember { mutableStateOf(ExportFormat.CSV) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Экспорт данных") },
        text = {
            Column {
                Text("Выберите формат экспорта:")
                Spacer(modifier = Modifier.height(8.dp))
                ExportFormat.entries.forEach { format ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { exportFormat = format }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = exportFormat == format,
                            onClick = { exportFormat = format }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(format.displayName)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onExport
            ) {
                Text("Экспортировать")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Отмена")
            }
        }
    )
}

enum class ExportFormat(val displayName: String) {
    CSV("CSV файл"),
    EXCEL("Excel файл"),
    JSON("JSON файл")
}