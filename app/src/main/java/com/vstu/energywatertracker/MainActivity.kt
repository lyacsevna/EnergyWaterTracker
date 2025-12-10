package com.vstu.energywatertracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.vstu.energywatertracker.presentation.navigation.MainNavHost
import com.vstu.energywatertracker.ui.theme.EnergyWaterTrackerTheme
import com.vstu.energywatertracker.util.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Создаем канал уведомлений при запуске (один раз)
        NotificationHelper.createNotificationChannel(this)

        setContent {
            EnergyWaterTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavHost()
                }
            }
        }
    }

    override fun onDestroy() {
        // Очистка ресурсов при необходимости
        super.onDestroy()
    }
}