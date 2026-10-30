
package com.quackmove.app.feature.chargers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quackmove.app.data.model.ChargingStation
import com.quackmove.app.data.repository.GoogleChargingRepository
import kotlinx.coroutines.launch

class ChargersViewModel : ViewModel() {

    // Estaciones obtenidas mediante Google Places
    var stations by mutableStateOf<List<ChargingStation>>(
        emptyList()
    )
        private set

    // Estado de la consulta
    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Filtros de la pantalla
    var availableOnly by mutableStateOf(false)
        private set

    var ccs2Only by mutableStateOf(false)
        private set

    var fastOnly by mutableStateOf(false)
        private set

    // Verificar qué información proporcionó Google
    val hasAvailability: Boolean
        get() = stations.any { it.available != null }

    val hasConnectorInfo: Boolean
        get() = stations.any { it.connector != null }

    val hasPowerInfo: Boolean
        get() = stations.any { it.powerKw != null }

    // Aplicar únicamente los filtros seleccionados
    val filteredStations: List<ChargingStation>
        get() = stations.filter { station ->

            (!availableOnly || station.available == true) &&
                    (!ccs2Only || station.connector == "CCS2") &&
                    (!fastOnly || (station.powerKw ?: 0) >= 50)
        }

    init {
        refreshStations()
    }

    // Consultar estaciones reales
    fun refreshStations() {

        if (isLoading) return

        viewModelScope.launch {

            isLoading = true
            errorMessage = null

            try {
                stations = GoogleChargingRepository.fetchStations()

            } catch (exception: Exception) {

                stations = emptyList()

                errorMessage =
                    "No se pudieron cargar las estaciones: " +
                            (exception.message ?: "Error desconocido")

            } finally {
                isLoading = false
            }
        }
    }

    fun toggleAvailableOnly() {
        if (hasAvailability) {
            availableOnly = !availableOnly
        }
    }

    fun toggleCcs2Only() {
        if (hasConnectorInfo) {
            ccs2Only = !ccs2Only
        }
    }

    fun toggleFastOnly() {
        if (hasPowerInfo) {
            fastOnly = !fastOnly
        }
    }
}
