package com.example.student.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Promedios : Screen("promedios")
    object AgregarEstudiante : Screen("agregar_estudiante")
}