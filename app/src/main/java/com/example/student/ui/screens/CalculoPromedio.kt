package com.example.student.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.student.viewmodel.EstudiantesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculoPromedioScreen(
    viewModel: EstudiantesViewModel
) {
    val estudiantes = viewModel.estudiantes.value
    val promedioGeneral = viewModel.calcularPromedioGeneral()
    val estudianteMayorPuntuaje = viewModel.obtenerEstudianteConMayorPuntuaje()
    val grupos = viewModel.obtenerGrupos()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Cálculos y Estadísticas") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Promedio General
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Promedio General",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "%.2f".format(promedioGeneral),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Total de estudiantes: ${estudiantes.size}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Estudiante con Mayor Puntuaje
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Estudiante con Mayor Puntuaje",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (estudianteMayorPuntuaje != null) {
                        Text(
                            text = estudianteMayorPuntuaje.nombreCompleto,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Grado ${estudianteMayorPuntuaje.grado} - Grupo ${estudianteMayorPuntuaje.grupo}")
                            Text(
                                text = "Puntuaje: ${estudianteMayorPuntuaje.puntuaje}",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        Text("No hay estudiantes registrados")
                    }
                }
            }

            // Top 3 por Grupo
            grupos.forEach { grupo ->
                val top3 = viewModel.obtenerTop3PorGrupo(grupo)
                val promedioGrupo = viewModel.calcularPromedioPorGrupo(grupo)

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Grupo $grupo - Top 3",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Promedio del grupo: %.2f".format(promedioGrupo),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        if (top3.isNotEmpty()) {
                            top3.forEachIndexed { index, estudiante ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${index + 1}. ${estudiante.nombreCompleto}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "${estudiante.puntuaje}",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = "No hay estudiantes en este grupo",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}