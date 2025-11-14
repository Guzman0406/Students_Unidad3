package com.example.student.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.student.data.Estudiante
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EstudiantesViewModel : ViewModel() {
    private val _estudiantes = MutableStateFlow(emptyList<Estudiante>())
    val estudiantes: StateFlow<List<Estudiante>> = _estudiantes

    private val _promedioGeneral = MutableStateFlow(0.0)
    val promedioGeneral: StateFlow<Double> = _promedioGeneral

    private val _mayorRezago = MutableStateFlow<Estudiante?>(null)
    val mayorRezago: StateFlow<Estudiante?> = _mayorRezago

    private var nextId = 0

    init {
        _estudiantes.value = listOf(
            Estudiante(nextId++, "Juan Pérez", "A", 7.5),
            Estudiante(nextId++, "Ana García", "B", 9.2),
            Estudiante(nextId++, "Carlos Lopez", "A", 6.0),
            Estudiante(nextId++, "Sofía Ruiz", "C", 8.8)
        )
        calcularEstadisticas()
    }

    fun agregarEstudiante(nombre: String, grupo: String, promedio: String) = viewModelScope.launch {
        val nuevoEstudiante = Estudiante(
            id = nextId++,
            nombre = nombre,
            grupo = grupo,
            promedio = promedio.toDoubleOrNull() ?: 0.0
        )
        _estudiantes.update { currentList ->
            currentList + nuevoEstudiante
        }
        calcularEstadisticas()
    }

    fun eliminarEstudiante(estudianteId: Int) = viewModelScope.launch {
        _estudiantes.update { currentList ->
            currentList.filter { it.id != estudianteId }
        }
        calcularEstadisticas()
    }

    private fun calcularEstadisticas() {
        val currentList = _estudiantes.value
        if (currentList.isEmpty()) {
            _promedioGeneral.value = 0.0
            _mayorRezago.value = null
            return
        }

        val sumatoria = currentList.sumOf { it.promedio }
        _promedioGeneral.value = sumatoria / currentList.size
        _mayorRezago.value = currentList.minByOrNull { it.promedio }
    }
}