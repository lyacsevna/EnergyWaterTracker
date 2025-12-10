package com.vstu.energywatertracker.presentation.screen.settings

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import com.vstu.energywatertracker.util.NotificationHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkThemeEnabled by remember { mutableStateOf(false) }
    var reminderDay by remember { mutableStateOf(25) }

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
                            if (enabled) {
                                NotificationHelper.scheduleMonthlyReminder(
                                    context,
                                    reminderDay
                                )
                            } else {
                                NotificationHelper.cancelReminders(context)
                            }
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
                            NotificationHelper.scheduleMonthlyReminder(
                                context,
                                reminderDay
                            )
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

        // Безопасность
        ListItem(
            headlineContent = { Text("Безопасность") },
            leadingContent = {
                Icon(Icons.Default.Security, contentDescription = null)
            }
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = { /* Экспорт данных */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Экспорт данных")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { /* Импорт данных */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Импорт данных")
                }
            }
        }

        // Хранилище
        ListItem(
            headlineContent = { Text("Хранилище") },
            leadingContent = {
                Icon(Icons.Default.Storage, contentDescription = null)
            }
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = { /* Очистить кэш */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Очистить кэш")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { /* Удалить все данные */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Удалить все данные", color = MaterialTheme.colorScheme.error)
                }
            }
        }

        // Информация о приложении
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
            }
        }
    }
}