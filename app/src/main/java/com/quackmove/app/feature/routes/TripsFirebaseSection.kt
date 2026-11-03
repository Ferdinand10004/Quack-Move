package com.quackmove.app.feature.routes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

// Colores de QuackMove
private val PanelColor = Color(0xFF192940)
private val BorderColor = Color(0xFF405174)
private val SecondaryText = Color(0xFFB6C2DF)
private val PurpleColor = Color(0xFF8066F5)
private val GreenColor = Color(0xFF43E6B3)
private val WarningColor = Color(0xFFFFB86B)

// Conecta la pantalla con el Repository y ViewModel.
@Composable
fun TripsFirebaseRoute(
    route: RouteSummary?,
    batteryPercent: Double?,
    modifier: Modifier = Modifier
) {

    // Detectar conexión a Internet.
    val isOnline = rememberInternetAvailable()

    // Repository de Firebase.
    val repository = remember {
        QuackMoveTripDependencies.tripRepository
    }

    // Crear el ViewModel mediante inyección manual.
    val factory = remember(repository) {
        TripsViewModelFactory(repository)
    }

    val tripsViewModel: TripsViewModel = viewModel(
        factory = factory
    )

    val state by tripsViewModel.uiState
        .collectAsStateWithLifecycle()

    TripsFirebaseContent(
        modifier = modifier,
        route = route,
        batteryPercent = batteryPercent,
        state = state,
        isOnline = isOnline,

        // Actualizar solamente con Internet.
        onRefresh = {
            if (isOnline) {
                tripsViewModel.loadTrips()
            }
        },

        // Guardar solamente con Internet.
        onSave = {

            val currentRoute = route
            val currentBattery = batteryPercent

            if (
                isOnline &&
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

// Pantalla de Firebase.
// No accede directamente a Firebase ni a Room.
@Composable
private fun TripsFirebaseContent(
    route: RouteSummary?,
    batteryPercent: Double?,
    state: TripsUiState,
    isOnline: Boolean,
    onRefresh: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {

    // Verificar que los datos permitan guardar.
    val canSave = isOnline &&
            route != null &&
            route.distanceKm > 0 &&
            route.drivingMinutes > 0 &&
            batteryPercent != null &&
            batteryPercent.isFinite() &&
            batteryPercent in 0.0..100.0 &&
            !state.isSaving

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = PanelColor,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            1.dp,
            BorderColor
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Título principal.
            Text(
                text = "Guardar viaje",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )

            // Estado de la conexión.
            if (!isOnline) {

                Surface(
                    color = Color(0xFF493821),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {

                        Text(
                            text = "Sin conexión a Internet",
                            color = WarningColor,
                            style = MaterialTheme.typography.titleSmall
                        )

                        Text(
                            text = "El guardado en Firebase está " +
                                    "deshabilitado temporalmente. " +
                                    "Puedes consultar tus viajes " +
                                    "en el historial local.",
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

            } else {

                Text(
                    text = "Conexión a Internet disponible",
                    color = GreenColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Información de la ruta.
            Text(
                text = if (route == null) {
                    "Primero necesitamos calcular una ruta."
                } else {
                    "${route.origin} → ${route.destination}"
                },
                color = SecondaryText
            )

            Text(
                text = "Se guardará la ruta calculada, " +
                        "no otro destino seleccionado en el mapa.",
                color = SecondaryText,
                style = MaterialTheme.typography.bodySmall
            )

            // Botón para guardar en Firestore.
            Button(
                onClick = onSave,
                enabled = canSave,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurpleColor,
                    disabledContainerColor = Color(0xFF405174)
                )
            ) {

                Text(
                    text = when {
                        state.isSaving -> "Guardando..."
                        !isOnline -> "Sin conexión"
                        else -> "Guardar viaje en Firebase"
                    }
                )
            }

            // Indicador de guardado.
            if (state.isSaving) {

                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = PurpleColor
                )
            }

            // Mensaje de confirmación.
            state.message?.let { message ->

                Text(
                    text = message,
                    color = GreenColor
                )
            }

            // Mensajes de error.
            state.errorMessage?.let { error ->

                Text(
                    text = error,
                    color = WarningColor
                )
            }

            HorizontalDivider(
                color = BorderColor
            )

            // Sección de viajes guardados.
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
                    enabled = isOnline &&
                            !state.isLoading &&
                            !state.isSaving
                ) {

                    Text("Actualizar")
                }
            }

            // Estado de carga.
            if (state.isLoading) {

                CircularProgressIndicator(
                    color = PurpleColor
                )

                Text(
                    text = "Cargando viajes...",
                    color = SecondaryText
                )

            } else if (state.trips.isEmpty()) {

                // Estado vacío.
                Text(
                    text = if (isOnline) {
                        "Todavía no hay viajes cargados."
                    } else {
                        "No hay viajes de Firebase cargados " +
                                "en esta pantalla. Consulta el historial local."
                    },
                    color = SecondaryText
                )

            } else {

                // Lista de viajes obtenidos.
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 220.dp),
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
                                    color = SecondaryText
                                )

                                Text(
                                    text = "${trip.distanceKm} km · " +
                                            "${trip.departureBatteryPercent}% " +
                                            "de batería inicial",
                                    color = GreenColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
