package com.vstu.energywatertracker.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vstu.energywatertracker.presentation.screen.input.InputScreen
import com.vstu.energywatertracker.presentation.screen.main.MainScreen
import com.vstu.energywatertracker.presentation.screen.service.ServiceScreen
import com.vstu.energywatertracker.presentation.screen.settings.SettingsScreen
import com.vstu.energywatertracker.presentation.screen.statistics.StatisticsScreen
import com.vstu.energywatertracker.presentation.viewmodel.MeterViewModel

@Composable
fun MainNavHost() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Main.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Main.route) {
                val viewModel: MeterViewModel = hiltViewModel()
                MainScreen(viewModel)
            }
            composable(Screen.Input.route) {
                val viewModel: MeterViewModel = hiltViewModel()
                InputScreen(
                    viewModel = viewModel,
                    onNavigateToCamera = { /* Навигация к камере */ }
                )
            }
            composable(Screen.Statistics.route) {
                val viewModel: MeterViewModel = hiltViewModel()
                StatisticsScreen(viewModel)
            }
            composable(Screen.Service.route) {
                ServiceScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    viewModel = TODO()
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val currentRoute = currentRoute(navController)

    NavigationBar {
        BottomNavItem.entries.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) }
            )
        }
    }
}

@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Input : Screen("input")
    object Statistics : Screen("statistics")
    object Service : Screen("service")
    object Settings : Screen("settings")
}

enum class BottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector
) {
    MAIN("Главная", Screen.Main.route, Icons.Default.Home),
    INPUT("Ввод", Screen.Input.route, Icons.Default.Edit),
    STATISTICS("Статистика", Screen.Statistics.route, Icons.Default.BarChart),
    SERVICE("Сервис", Screen.Service.route, Icons.Default.Map),
    SETTINGS("Настройки", Screen.Settings.route, Icons.Default.Settings)
}