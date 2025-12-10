package com.vstu.energywatertracker.presentation.screen.service

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun ServiceScreen() {
    var showMap by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Переключатель вид/карта - простые кнопки
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { showMap = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!showMap) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Список")
            }

            Spacer(modifier = Modifier.width(1.dp))

            Button(
                onClick = { showMap = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (showMap) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Карта")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (showMap) {
            // Карта (заглушка)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Карта сервисных центров\n(будет реализована с Google Maps)")
            }
        } else {
            // Список сервисных центров
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(5) { index ->
                    ServiceCenterCard(index = index + 1)
                }
            }
        }
    }
}

@Composable
fun ServiceCenterCard(index: Int) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Сервисный центр №$index",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Адрес
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("ул. Ленина, $index")
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Часы работы
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("9:00-18:00")
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Телефон
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("+7 (473) 123-45-6$index")
            }
        }
    }
}