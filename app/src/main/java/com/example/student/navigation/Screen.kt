package com.example.student.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Promedios : Screen("promedios")
    object AgregarEstudiante : Screen("agregar_estudiante")
    object EditarEstudiante : Screen("editar_estudiante/{estudianteId}") {
        fun createRoute(estudianteId: String) = "editar_estudiante/$estudianteId"
    }
}