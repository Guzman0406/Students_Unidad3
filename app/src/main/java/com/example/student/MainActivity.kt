package com.example.student

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.student.navigation.Screen
import com.example.student.ui.screens.AgregarEstudiantesScreen
import com.example.student.ui.screens.CalculoPromedioScreen
import com.example.student.ui.screens.DashboardScreen
import com.example.student.ui.screens.EditarEstudianteScreen
import com.example.student.ui.theme.StudentTheme
import com.example.student.viewmodel.EstudiantesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentTheme {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(viewModel: EstudiantesViewModel = viewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Estudiantes") },
                    label = { Text("Estudiantes") },
                    selected = currentDestination?.hierarchy?.any { it.route == Screen.Dashboard.route } == true,
                    onClick = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Assessment, contentDescription = "Promedios") },
                    label = { Text("Promedios") },
                    selected = currentDestination?.hierarchy?.any { it.route == Screen.Promedios.route } == true,
                    onClick = {
                        navController.navigate(Screen.Promedios.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (currentDestination?.route == Screen.Dashboard.route) {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.AgregarEstudiante.route) }
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar Estudiante")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(navController, viewModel)
            }
            composable(Screen.Promedios.route) {
                CalculoPromedioScreen(viewModel)
            }
            composable(Screen.AgregarEstudiante.route) {
                AgregarEstudiantesScreen(navController, viewModel)
            }
            composable(
                route = Screen.EditarEstudiante.route,
                arguments = listOf(navArgument("estudianteId") { type = NavType.StringType })
            ) { backStackEntry ->
                val estudianteId = backStackEntry.arguments?.getString("estudianteId")
                EditarEstudianteScreen(
                    navController = navController,
                    viewModel = viewModel,
                    estudianteId = estudianteId
                )
            }
        }
    }
}