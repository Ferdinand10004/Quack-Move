
package com.quackmove.app.feature.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.quackmove.app.domain.SavedTrip
import com.quackmove.app.domain.TripRepository
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado inmutable de la pantalla
data class TripsUiState(
    val trips: List<SavedTrip> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null
)

class TripsViewModel(
    private val repository: TripRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadTrips()
    }

    // Consultar viajes guardados en Firestore
    fun loadTrips() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                val trips = repository.getTrips()

                _uiState.update {
                    it.copy(trips = trips)
                }

            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "No se pudieron cargar los viajes."
                    )
                }
            } finally {
                _uiState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    // Guardar un viaje desde una acción del usuario
    fun saveTrip(trip: SavedTrip) {
        if (_uiState.value.isSaving) return

        if (
            trip.origin.isBlank() ||
            trip.destination.isBlank() ||
            trip.distanceKm <= 0 ||
            trip.drivingMinutes <= 0 ||
            !trip.departureBatteryPercent.isFinite() ||
            trip.departureBatteryPercent !in 0.0..100.0
        ) {
            _uiState.update {
                it.copy(
                    errorMessage = "Los datos del viaje no son válidos."
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                isSaving = true,
                message = null,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            try {
                repository.saveTrip(trip)

                _uiState.update {
                    it.copy(
                        message = "Viaje guardado en Firestore."
                    )
                }

                loadTrips()

            } catch (exception: TimeoutCancellationException) {
                _uiState.update {
                    it.copy(
                        message = "La confirmación está pendiente. " +
                                "Comprueba la conexión antes de reintentar."
                    )
                }

            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "No se pudo confirmar el guardado: " +
                                (exception.message ?: "Error desconocido")
                    )
                }

            } finally {
                _uiState.update {
                    it.copy(isSaving = false)
                }
            }
        }
    }
}

// Inyección manual del Repository
class TripsViewModelFactory(
    private val repository: TripRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(TripsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TripsViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}
