package com.example.student.data


data class Estudiante(
    val id: Int = 0,
    val nombre: String = "",
    val apellido: String = "",
    val grado: String = "",
    val grupo: String = "",
    val puntuaje: Double = 0.0
) {
    val nombreCompleto: String
        get() = "$nombre $apellido"
}