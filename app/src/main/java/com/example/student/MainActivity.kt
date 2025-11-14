package com.example.student

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.student.navigation.Screen
import com.example.student.ui.screens.*
import com.example.myproject.ui.theme.MyProjectTheme
import com.example.myproject.viewmodel.EstudiantesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyProjectTheme {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(viewModel: EstudiantesViewModel = viewModel()) {
    val navController = rememberNavController()

    // Definición de las pestañas de la barra inferior
    val items = listOf(Screen.Dashboard, Screen.Promedios)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        icon = {
                            val icon = when (screen) {
                                Screen.Dashboard -> Icons.Filled.List
                                Screen.Promedios -> Icons.Filled.Settings
                                else -> Icons.Filled.List // Fallback
                            }
                            Icon(icon, contentDescription = screen.route)
                        },
                        label = { Text(screen.route.capitalize()) },
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Evita construir una gran pila de destinos en la pila posterior
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Evita múltiples copias del mismo destino
                                launchSingleTop = true
                                // Restaura el estado al cambiar de pestaña
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            if (currentDestination?.route == Screen.Dashboard.route) {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.AgregarEstudiante.route) }
                ) {
                    Icon(Icons.Filled.Add, "Agregar Estudiante")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Pestañas principales
            composable(Screen.Dashboard.route) {
                EstudiantesDashboardScreen(navController, viewModel)
            }
            composable(Screen.Promedios.route) {
                CalculoPromediosScreen(viewModel)
            }

            // Pantalla de formulario (navegación profunda)
            composable(Screen.AgregarEstudiante.route) {
                AgregarEstudianteScreen(navController, viewModel)
            }
        }
    }
}