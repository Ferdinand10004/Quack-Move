package com.quackmove.app.feature.routes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import com.quackmove.app.data.repository.GoogleRoutesRepository
import java.util.Locale
import kotlin.math.roundToInt

// Colores originales de QuackMove
private val RouteBackground = Color(0xFF101928)
private val RoutePanel = Color(0xFF192940)
private val RouteBorder = Color(0xFF405174)
private val RouteText = Color(0xFFF5F6FF)
private val RouteSecondary = Color(0xFFB6C2DF)
private val RoutePurple = Color(0xFF8066F5)
private val RouteGreen = Color(0xFF43E6B3)
private val RouteWarning = Color(0xFFFFB86B)

private fun formatNumber(value: Double): String {
    return String.format(Locale.US, "%.1f", value)
}

@Composable
fun RoutesScreen(
    destination: String,
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    // Inyección manual del Repository mediante Factory
    val factory = remember {
        RoutesViewModelFactory(
            repository = GoogleRoutesRepository
        )
    }

    val model: RoutesViewModel = viewModel(
        factory = factory
    )

    // Observar StateFlow
    val state by model.uiState.collectAsStateWithLifecycle()

    val route = state.route
    val estimate = state.batteryEstimate

    val batteryLevel = state.departureBattery
        .toFloatOrNull()
        ?.coerceIn(0f, 100f)
        ?: 80f

    Scaffold(
        modifier = modifier,
        containerColor = RouteBackground,

        bottomBar = {

            Surface(color = RouteBackground) {

                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp)
                        .height(54.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RoutePurple
                    )
                ) {

                    Icon(
                        imageVector =
                            Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )

                    Spacer(Modifier.width(12.dp))

                    Text("Volver al mapa")
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            Text(
                text = "Planificador de rutas",
                color = RouteText,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )

            // Vista previa del mapa
            GoogleMapPreview(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            // =====================================
            // VEHÍCULO ELÉCTRICO
            // =====================================

            RouteCard {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Column {

                        Text(
                            text = "Mi vehículo eléctrico",
                            color = RouteText,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "Vehículo de prueba",
                            color = RouteSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Icon(
                        imageVector =
                            Icons.Default.DirectionsCar,
                        contentDescription = "Vehículo eléctrico",
                        tint = RouteGreen,
                        modifier = Modifier.size(46.dp)
                    )
                }

                HorizontalDivider(color = RouteBorder)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = "Nivel de batería",
                        color = RouteText,
                        fontSize = 16.sp
                    )

                    Text(
                        text = "${batteryLevel.roundToInt()}%",
                        color = RouteGreen,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Slider(
                    value = batteryLevel,

                    onValueChange = { value ->
                        model.updateDepartureBattery(
                            value.roundToInt().toString()
                        )
                    },

                    valueRange = 0f..100f,

                    colors = SliderDefaults.colors(
                        thumbColor = RouteGreen,
                        activeTrackColor = RouteGreen,
                        inactiveTrackColor = RouteBorder
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "0%",
                        color = RouteSecondary,
                        fontSize = 12.sp
                    )

                    Text(
                        text = "100%",
                        color = RouteSecondary,
                        fontSize = 12.sp
                    )
                }

                Text(
                    text = "Desliza para indicar la carga actual.",
                    color = RouteSecondary,
                    fontSize = 12.sp
                )

                Text(
                    text = "Los datos técnicos son provisionales " +
                            "hasta conectar el catálogo de vehículos.",
                    color = RouteSecondary,
                    fontSize = 11.sp
                )
            }

            // =====================================
            // INFORMACIÓN DE LA RUTA
            // =====================================

            RouteCard {

                Text(
                    text = "Tu viaje",
                    color = RouteText,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.SemiBold
                )

                if (destination.isNotBlank()) {

                    Text(
                        text = "Destino seleccionado en el mapa: " +
                                destination,
                        color = RouteSecondary,
                        fontSize = 12.sp
                    )
                }

                Text(
                    text = "Ruta de prueba: Ciudad de Guatemala → " +
                            "Flores, Petén",
                    color = RouteSecondary,
                    fontSize = 12.sp
                )

                if (state.isLoading) {

                    CircularProgressIndicator(
                        color = RoutePurple
                    )
                }

                state.errorMessage?.let { error ->

                    Text(
                        text = error,
                        color = RouteWarning
                    )
                }

                if (route != null) {

                    HorizontalDivider(color = RouteBorder)

                    RouteInfoRow(
                        label = "Origen",
                        value = route.origin
                    )

                    RouteInfoRow(
                        label = "Destino",
                        value = route.destination
                    )

                    RouteInfoRow(
                        label = "Distancia",
                        value =
                            "${formatNumber(route.distanceKm)} km"
                    )

                    RouteInfoRow(
                        label = "Conducción",
                        value =
                            "${route.drivingMinutes / 60} h " +
                                    "${route.drivingMinutes % 60} min"
                    )

                    Text(
                        text = "Estimación sin tráfico en tiempo real.",
                        color = RouteSecondary,
                        fontSize = 12.sp
                    )
                }

                TextButton(
                    onClick = model::refreshRoute,
                    enabled = !state.isLoading
                ) {
                    Text("Actualizar ruta")
                }
            }

            // =====================================
            // FIREBASE
            // =====================================

            TripsFirebaseRoute(
                route = route,
                batteryPercent =
                    state.departureBattery.toDoubleOrNull()
            )

            // =====================================
            // ROOM
            // =====================================

            TripsLocalSection(
                route = route,
                batteryPercent =
                    state.departureBattery.toDoubleOrNull()
            )

            // =====================================
            // CÁLCULO DE AUTONOMÍA
            // =====================================

            if (estimate != null) {

                RouteCard {

                    Text(
                        text = "Estado estimado del viaje",
                        color = RouteText,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    RouteInfoRow(
                        label = "Energía necesaria",
                        value =
                            "${formatNumber(estimate.energyRequiredKwh)} kWh"
                    )

                    RouteInfoRow(
                        label = "Batería consumida",
                        value =
                            "${formatNumber(estimate.batteryUsedPercent)}%"
                    )

                    RouteInfoRow(
                        label = "Batería estimada al llegar",
                        value =
                            estimate.estimatedArrivalPercent
                                ?.let {
                                    "${formatNumber(it)}%"
                                }
                                ?: "No alcanza"
                    )

                    HorizontalDivider(color = RouteBorder)

                    if (estimate.canCompleteTrip) {

                        Text(
                            text = "Tu batería alcanza para el viaje " +
                                    "con la reserva prevista.",
                            color = RouteGreen,
                            fontWeight = FontWeight.SemiBold
                        )

                    } else {

                        Text(
                            text = "Necesitarás recargar durante el viaje.",
                            color = RouteWarning,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "Energía adicional estimada: " +
                                    "${formatNumber(estimate.missingEnergyKwh)} kWh",
                            color = RouteSecondary
                        )
                    }

                    Text(
                        text = "Estimación orientativa. El consumo real " +
                                "depende de las condiciones de conducción.",
                        color = RouteSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            state.validationError?.let { error ->

                Text(
                    text = error,
                    color = RouteWarning,
                    fontSize = 13.sp
                )
            }
        }
    }
}

// =====================================
// TARJETA REUTILIZABLE
// =====================================

@Composable
private fun RouteCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = RoutePanel,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, RouteBorder)
    ) {

        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            content = content
        )
    }
}

// =====================================
// FILA REUTILIZABLE
// =====================================

@Composable
private fun RouteInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween,
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(
            text = label,
            color = RouteSecondary,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = value,
            color = RouteText,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
