package com.vstu.energywatertracker.presentation.screen.input

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.vstu.energywatertracker.data.local.entity.MeterReading
import com.vstu.energywatertracker.data.local.entity.MeterType
import com.vstu.energywatertracker.presentation.viewmodel.MeterViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    viewModel: MeterViewModel,
    onNavigateToCamera: () -> Unit
) {
    var selectedType by remember { mutableStateOf(MeterType.ELECTRICITY) }
    var value by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Камера будет реализована позже
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Выбор типа счетчика
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = getMeterTypeDisplayName(selectedType),
                onValueChange = {},
                readOnly = true,
                label = { Text("Тип счетчика") },
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                MeterType.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(getMeterTypeDisplayName(type)) },
                        onClick = {
                            selectedType = type
                            expanded = false
                            // Загрузить последнее показание для подсказки
                            val latest = viewModel.getLatestReading(type)
                            value = latest?.value?.toString() ?: ""
                        }
                    )
                }
            }
        }

        // Поле ввода значения
        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            label = { Text("Показания") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // Кнопки для сканирования
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    if (hasCameraPermission(context)) {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Сканировать (скоро)")
            }

            Button(
                onClick = { /* Загрузка из галереи - скоро */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Из галереи")
            }
        }

        // Поле для заметок
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Заметки") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            singleLine = false
        )

        // Кнопка сохранения
        Button(
            onClick = {
                val readingValue = value.toDoubleOrNull()
                if (readingValue != null) {
                    val reading = MeterReading(
                        type = selectedType,
                        value = readingValue,
                        date = Date(),
                        notes = notes
                    )
                    viewModel.addReading(reading)
                    value = ""
                    notes = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = value.isNotEmpty()
        ) {
            Text("Сохранить")
        }
    }
}

private fun getMeterTypeDisplayName(type: MeterType): String {
    return when (type) {
        MeterType.ELECTRICITY -> "Электричество"
        MeterType.WATER_COLD -> "Холодная вода"
        MeterType.WATER_HOT -> "Горячая вода"
    }
}

private fun hasCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}