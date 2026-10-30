
package com.quackmove.app.feature.routes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
    onBack: () -> Unit = {}
) {
    val model: RoutesViewModel = viewModel()

    val route = model.route
    val estimate = model.batteryEstimate

    val batteryLevel = model.departureBattery
        .toFloatOrNull()
        ?.coerceIn(0f, 100f)
        ?: 80f

    Scaffold(
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
                        Icons.Default.ArrowBack,
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

            GoogleMapPreview(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )


            // Tarjeta del vehículo
            RouteCard {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
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
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = "Vehículo eléctrico",
                        tint = RouteGreen,
                        modifier = Modifier.size(46.dp)
                    )
                }

                HorizontalDivider(color = RouteBorder)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
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

                // Barra interactiva de batería
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
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "0%",
                        color = RouteSecondary,
                        fontSize = 12.sp
                    )

                    Text(
                        "100%",
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

            // Información de Google Routes
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

                if (model.isLoading) {
                    CircularProgressIndicator(
                        color = RoutePurple
                    )
                }

                model.errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = RouteWarning
                    )
                }

                if (route != null) {

                    HorizontalDivider(color = RouteBorder)

                    RouteInfoRow(
                        "Origen",
                        route.origin
                    )

                    RouteInfoRow(
                        "Destino",
                        route.destination
                    )

                    RouteInfoRow(
                        "Distancia",
                        "${formatNumber(route.distanceKm)} km"
                    )

                    RouteInfoRow(
                        "Conducción",
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
                    enabled = !model.isLoading
                ) {
                    Text("Actualizar ruta")
                }
            }

            TripsFirebaseRoute(
                route = route,
                batteryPercent = model.departureBattery.toDoubleOrNull()
            )

            // Cálculos automáticos
            if (estimate != null) {

                RouteCard {

                    Text(
                        text = "Estado estimado del viaje",
                        color = RouteText,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    RouteInfoRow(
                        "Energía necesaria",
                        "${formatNumber(estimate.energyRequiredKwh)} kWh"
                    )

                    RouteInfoRow(
                        "Batería consumida",
                        "${formatNumber(estimate.batteryUsedPercent)}%"
                    )

                    RouteInfoRow(
                        "Batería estimada al llegar",
                        estimate.estimatedArrivalPercent
                            ?.let { "${formatNumber(it)}%" }
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
        }
    }
}

@Composable
private fun RouteCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
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

@Composable
private fun RouteInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
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
