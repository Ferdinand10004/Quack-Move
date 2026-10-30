
package com.quackmove.app.feature.chargers

import com.quackmove.app.data.model.ChargingStation
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EvStation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores originales de QuackMove
private val ChargingBackground = Color(0xFF101928)
private val ChargingSurface = Color(0xFF192940)
private val ChargingBorder = Color(0xFF405174)
private val ChargingText = Color(0xFFF5F6FF)
private val ChargingSecondaryText = Color(0xFFB6C2DF)
private val ChargingGreen = Color(0xFF43E6B3)
private val ChargingPurple = Color(0xFFB6A0FF)

@Composable
fun ChargersScreen(
    onBack: () -> Unit = {}
) {
    val model: ChargersViewModel = viewModel()
    val filteredStations = model.filteredStations

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ChargingBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {

            // Encabezado original
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver al mapa",
                        tint = ChargingText
                    )
                }

                Column {
                    Text(
                        text = "Cargadores cercanos",
                        color = ChargingText,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "Estaciones consultadas en Google",
                        color = ChargingSecondaryText,
                        fontSize = 12.sp
                    )
                }
            }

            // Filtros de estaciones
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = model.availableOnly,
                    enabled = model.hasAvailability,
                    onClick = {
                        model.toggleAvailableOnly()
                    },
                    label = {
                        Text("Disponibles")
                    }
                )

                FilterChip(
                    selected = model.ccs2Only,
                    enabled = model.hasConnectorInfo,
                    onClick = {
                        model.toggleCcs2Only()
                    },
                    label = {
                        Text("CCS2")
                    }
                )

                FilterChip(
                    selected = model.fastOnly,
                    enabled = model.hasPowerInfo,
                    onClick = {
                        model.toggleFastOnly()
                    },
                    label = {
                        Text("Rápidos")
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Resultados y actualización
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (model.isLoading) {
                        "Buscando estaciones..."
                    } else {
                        "${filteredStations.size} estaciones encontradas"
                    },
                    color = ChargingSecondaryText,
                    fontSize = 13.sp
                )

                TextButton(
                    onClick = {
                        model.refreshStations()
                    },
                    enabled = !model.isLoading
                ) {
                    Text(
                        text = "Actualizar",
                        color = ChargingGreen
                    )
                }
            }

            // Atribución de Google
            Text(
                text = "Google Maps",
                color = ChargingText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 4.dp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Lista de estaciones
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // Indicador mientras consulta Google
                if (model.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = ChargingGreen
                            )
                        }
                    }
                }

                // Error de conexión
                model.errorMessage?.let { message ->
                    item {
                        Text(
                            text = message,
                            color = Color(0xFFFFB86B),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                // Estaciones obtenidas
                items(filteredStations) { station ->
                    ChargingStationCard(station)
                }

                // No se encontraron estaciones
                if (
                    !model.isLoading &&
                    model.errorMessage == null &&
                    filteredStations.isEmpty()
                ) {
                    item {
                        Text(
                            text = "No se encontraron estaciones que coincidan con los filtros.",
                            color = ChargingSecondaryText,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChargingStationCard(
    station: ChargingStation
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = ChargingSurface,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 1.dp,
            color = ChargingBorder
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.EvStation,
                    contentDescription = null,
                    tint = ChargingGreen,
                    modifier = Modifier.size(32.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = station.name,
                        color = ChargingText,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )

                    Text(
                        text = listOf(
                            station.location,
                            station.distance
                        )
                            .filter { it.isNotBlank() }
                            .joinToString(" · "),
                        color = ChargingSecondaryText,
                        fontSize = 12.sp
                    )
                }
            }

            HorizontalDivider(
                color = ChargingBorder
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = station.connector
                        ?: "Conector sin datos",
                    color = ChargingPurple,
                    fontSize = 12.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = ChargingGreen,
                        modifier = Modifier.size(18.dp)
                    )

                    Text(
                        text = station.powerKw?.let {
                            "$it kW"
                        } ?: "Potencia sin datos",
                        color = ChargingGreen,
                        fontSize = 12.sp
                    )
                }

                Text(
                    text = when (station.available) {
                        true -> "Disponible"
                        false -> "Ocupado"
                        null -> "Sin datos"
                    },
                    color = when (station.available) {
                        true -> ChargingGreen
                        false -> Color(0xFFFFB86B)
                        null -> ChargingSecondaryText
                    },
                    fontSize = 12.sp
                )
            }
        }
    }
}
