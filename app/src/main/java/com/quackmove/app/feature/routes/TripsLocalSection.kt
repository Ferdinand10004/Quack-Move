package com.quackmove.app.feature.routes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quackmove.app.data.local.QuackMoveDatabase
import com.quackmove.app.data.repository.RoomTripRepository
import com.quackmove.app.data.repository.RouteSummary
import com.quackmove.app.domain.SavedTrip

@Composable
fun TripsLocalSection(
    route: RouteSummary?,
    batteryPercent: Double?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current.applicationContext

    val repository = remember(context) {
        RoomTripRepository(
            QuackMoveDatabase
                .getDatabase(context)
                .localTripDao()
        )
    }

    val factory = remember(repository) {
        TripsViewModelFactory(
            repository = repository,
            successMessage = "Viaje guardado en el teléfono."
        )
    }

    val model: TripsViewModel = viewModel(
        key = "local-trips",
        factory = factory
    )

    val state by model.uiState.collectAsStateWithLifecycle()

    val canSave = route != null &&
            route.distanceKm > 0 &&
            route.drivingMinutes > 0 &&
            batteryPercent != null &&
            batteryPercent.isFinite() &&
            batteryPercent in 0.0..100.0 &&
            !state.isSaving

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFF192940),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Historial local",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Tus viajes guardados en este teléfono.",
                color = Color(0xFFB6C2DF)
            )

            Button(
                enabled = canSave,
                onClick = {
                    val currentRoute = route
                    val currentBattery = batteryPercent

                    if (currentRoute != null &&
                        currentBattery != null
                    ) {
                        model.saveTrip(
                            SavedTrip(
                                origin = currentRoute.origin,
                                destination = currentRoute.destination,
                                distanceKm = currentRoute.distanceKm,
                                drivingMinutes =
                                    currentRoute.drivingMinutes,
                                departureBatteryPercent =
                                    currentBattery
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (state.isSaving) {
                        "Guardando..."
                    } else {
                        "Guardar en el dispositivo"
                    }
                )
            }

            state.message?.let {
                Text(
                    text = it,
                    color = Color(0xFF43E6B3)
                )
            }

            state.errorMessage?.let {
                Text(
                    text = it,
                    color = Color(0xFFFFB86B)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Viajes locales",
                    color = Color.White
                )

                TextButton(
                    onClick = model::loadTrips,
                    enabled = !state.isLoading
                ) {
                    Text("Actualizar")
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator()
            } else if (state.trips.isEmpty()) {
                Text(
                    text = "No hay viajes guardados localmente.",
                    color = Color(0xFFB6C2DF)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.trips,
                        key = { it.id }
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
                                    text = "${trip.distanceKm} km",
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
