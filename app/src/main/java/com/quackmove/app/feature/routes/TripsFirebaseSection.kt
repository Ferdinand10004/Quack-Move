
package com.quackmove.app.feature.routes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quackmove.app.data.repository.QuackMoveTripDependencies
import com.quackmove.app.data.repository.RouteSummary
import com.quackmove.app.domain.SavedTrip

// Contenedor: conecta Repository, ViewModel y pantalla
@Composable
fun TripsFirebaseRoute(
    route: RouteSummary?,
    batteryPercent: Double?
) {
    val repository = remember {
        QuackMoveTripDependencies.tripRepository
    }

    val factory = remember(repository) {
        TripsViewModelFactory(repository)
    }

    val tripsViewModel: TripsViewModel = viewModel(
        factory = factory
    )

    val state by tripsViewModel.uiState.collectAsStateWithLifecycle()

    TripsFirebaseContent(
        route = route,
        batteryPercent = batteryPercent,
        state = state,
        onRefresh = tripsViewModel::loadTrips,
        onSave = {
            val currentRoute = route
            val currentBattery = batteryPercent

            if (
                currentRoute != null &&
                currentBattery != null
            ) {
                tripsViewModel.saveTrip(
                    SavedTrip(
                        origin = currentRoute.origin,
                        destination = currentRoute.destination,
                        distanceKm = currentRoute.distanceKm,
                        drivingMinutes = currentRoute.drivingMinutes,
                        departureBatteryPercent = currentBattery
                    )
                )
            }
        }
    )
}

// Pantalla: recibe datos y acciones, sin utilizar Firebase
@Composable
private fun TripsFirebaseContent(
    route: RouteSummary?,
    batteryPercent: Double?,
    state: TripsUiState,
    onRefresh: () -> Unit,
    onSave: () -> Unit
) {
    val canSave = route != null &&
            route.distanceKm > 0 &&
            route.drivingMinutes > 0 &&
            batteryPercent != null &&
            batteryPercent.isFinite() &&
            batteryPercent in 0.0..100.0 &&
            !state.isSaving

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF192940),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            1.dp,
            Color(0xFF405174)
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Guardar viaje",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = if (route == null) {
                    "Primero necesitamos calcular una ruta."
                } else {
                    "${route.origin} → ${route.destination}"
                },
                color = Color(0xFFB6C2DF)
            )

            Text(
                text = "Se guardará la ruta calculada, " +
                        "no otro destino seleccionado en el mapa.",
                color = Color(0xFFB6C2DF),
                style = MaterialTheme.typography.bodySmall
            )

            Button(
                onClick = onSave,
                enabled = canSave,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8066F5)
                )
            ) {
                Text(
                    if (state.isSaving) {
                        "Guardando..."
                    } else {
                        "Guardar viaje en Firebase"
                    }
                )
            }

            if (state.isSaving) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            state.message?.let { message ->
                Text(
                    text = message,
                    color = Color(0xFF43E6B3)
                )
            }

            state.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = Color(0xFFFFB86B)
                )
            }

            HorizontalDivider(
                color = Color(0xFF405174)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Mis viajes guardados",
                    color = Color.White
                )

                TextButton(
                    onClick = onRefresh,
                    enabled = !state.isLoading &&
                            !state.isSaving
                ) {
                    Text("Actualizar")
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    color = Color(0xFF8066F5)
                )
            } else if (state.trips.isEmpty()) {
                Text(
                    text = "Todavía no hay viajes cargados.",
                    color = Color(0xFFB6C2DF)
                )
            } else {

                // Altura limitada porque la pantalla
                // principal ya tiene desplazamiento vertical.
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.trips,
                        key = { trip -> trip.id }
                    ) { trip ->

                        Surface(
                            color = Color(0xFF101928),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement =
                                    Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = trip.destination,
                                    color = Color.White
                                )

                                Text(
                                    text = "Desde: ${trip.origin}",
                                    color = Color(0xFFB6C2DF)
                                )

                                Text(
                                    text = "${trip.distanceKm} km · " +
                                            "${trip.departureBatteryPercent}% " +
                                            "de batería inicial",
                                    color = Color(0xFF43E6B3)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
