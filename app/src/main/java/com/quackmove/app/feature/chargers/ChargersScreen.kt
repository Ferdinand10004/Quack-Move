package com.quackmove.app.feature.chargers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EvStation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quackmove.app.data.model.ChargingStation
import com.quackmove.app.data.repository.GoogleChargingRepository
import com.quackmove.app.feature.routes.rememberInternetAvailable

// =====================================
// COLORES DE QUACKMOVE
// =====================================

private val ChargingBackground = Color(0xFF101928)
private val ChargingSurface = Color(0xFF192940)
private val ChargingBorder = Color(0xFF405174)
private val ChargingText = Color(0xFFF5F6FF)
private val ChargingSecondaryText = Color(0xFFB6C2DF)
private val ChargingGreen = Color(0xFF43E6B3)
private val ChargingPurple = Color(0xFFB6A0FF)
private val ChargingWarning = Color(0xFFFFB86B)
private val OfflineBackground = Color(0xFF3D3028)

// =====================================
// PANTALLA PRINCIPAL DE CARGADORES
// =====================================

@Composable
fun ChargersScreen(
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    // Inyección manual del Repository
    val factory = remember {
        ChargersViewModelFactory(
            repository = GoogleChargingRepository
        )
    }

    // Crear ViewModel mediante su Factory
    val model: ChargersViewModel = viewModel(
        factory = factory
    )

    // Observar el StateFlow del ViewModel
    val state by model.uiState.collectAsStateWithLifecycle()

    // Consultar estado de Internet
    val internetAvailable = rememberInternetAvailable()

    // Estaciones filtradas
    val filteredStations = state.filteredStations

    Surface(
        modifier = modifier.fillMaxSize(),
        color = ChargingBackground
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {

            // =====================================
            // ENCABEZADO
            // =====================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = onBack
                ) {

                    Icon(
                        imageVector =
                            Icons.AutoMirrored.Filled.ArrowBack,
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

            // =====================================
            // M10: ESTADO SIN CONEXIÓN
            // =====================================

            if (!internetAvailable) {

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = OfflineBackground,
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(6.dp)
                    ) {

                        Text(
                            text = "Sin conexión a Internet",
                            color = ChargingWarning,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )

                        Text(
                            text = if (state.stations.isNotEmpty()) {
                                "Se muestran los cargadores " +
                                        "cargados anteriormente. " +
                                        "Activa Internet para actualizar."
                            } else {
                                "Conéctate a Internet para " +
                                        "consultar las estaciones de carga."
                            },
                            color = ChargingSecondaryText,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }

            // =====================================
            // FILTROS DE ESTACIONES
            // =====================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                // Disponibilidad
                FilterChip(
                    selected = state.availableOnly,
                    enabled = state.hasAvailability,
                    onClick = model::toggleAvailableOnly,
                    label = {
                        Text("Disponibles")
                    }
                )

                // Conectores CCS2
                FilterChip(
                    selected = state.ccs2Only,
                    enabled = state.hasConnectorInfo,
                    onClick = model::toggleCcs2Only,
                    label = {
                        Text("CCS2")
                    }
                )

                // Carga rápida
                FilterChip(
                    selected = state.fastOnly,
                    enabled = state.hasPowerInfo,
                    onClick = model::toggleFastOnly,
                    label = {
                        Text("Rápidos")
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // =====================================
            // RESULTADOS Y ACTUALIZAR
            // =====================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = if (state.isLoading) {
                        "Buscando estaciones..."
                    } else {
                        "${filteredStations.size} estaciones encontradas"
                    },
                    color = ChargingSecondaryText,
                    fontSize = 13.sp
                )

                // Deshabilitar si no hay Internet
                // o ya hay una consulta en proceso
                TextButton(
                    onClick = model::refreshStations,
                    enabled = !state.isLoading &&
                            internetAvailable
                ) {

                    Text(
                        text = "Actualizar",
                        color = if (internetAvailable) {
                            ChargingGreen
                        } else {
                            ChargingSecondaryText
                        }
                    )
                }
            }

            // =====================================
            // ATRIBUCIÓN DE GOOGLE
            // =====================================

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

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // =====================================
            // LISTA DE ESTACIONES
            // =====================================

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 24.dp
                ),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                // =====================================
                // M10: ESTADO DE CARGA
                // =====================================

                if (state.isLoading) {

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

                // =====================================
                // M10: ESTADO DE ERROR
                // =====================================

                state.errorMessage?.let { message ->

                    item {

                        Text(
                            text = message,
                            color = ChargingWarning,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                // =====================================
                // RESULTADOS OBTENIDOS
                // =====================================

                items(
                    items = filteredStations
                ) { station ->

                    ChargingStationCard(
                        station = station
                    )
                }

                // =====================================
                // M10: ESTADO VACÍO
                // =====================================

                if (
                    !state.isLoading &&
                    state.errorMessage == null &&
                    filteredStations.isEmpty()
                ) {

                    item {

                        Text(
                            text = if (!internetAvailable) {
                                "No hay estaciones cargadas. " +
                                        "Conéctate a Internet para buscarlas."
                            } else {
                                "No se encontraron estaciones " +
                                        "que coincidan con los filtros."
                            },
                            color = ChargingSecondaryText,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

// =====================================
// COMPONENTE REUTILIZABLE
// TARJETA DE ESTACIÓN
// =====================================

@Composable
private fun ChargingStationCard(
    station: ChargingStation,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = ChargingSurface,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 1.dp,
            color = ChargingBorder
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            // =====================================
            // NOMBRE Y DIRECCIÓN
            // =====================================

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
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

            // =====================================
            // INFORMACIÓN DE CARGA
            // =====================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Tipo de conector
                Text(
                    text = station.connector
                        ?: "Conector sin datos",
                    color = ChargingPurple,
                    fontSize = 12.sp
                )

                // Potencia
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

                // Disponibilidad
                Text(
                    text = when (station.available) {
                        true -> "Disponible"
                        false -> "Ocupado"
                        null -> "Sin datos"
                    },
                    color = when (station.available) {
                        true -> ChargingGreen
                        false -> ChargingWarning
                        null -> ChargingSecondaryText
                    },
                    fontSize = 12.sp
                )
            }
        }
    }
}
