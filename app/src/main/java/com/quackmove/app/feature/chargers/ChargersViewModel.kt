package com.quackmove.app.feature.chargers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.quackmove.app.data.model.ChargingStation
import com.quackmove.app.data.repository.ChargingRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado de la pantalla de cargadores.
data class ChargersUiState(
    val stations: List<ChargingStation> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val availableOnly: Boolean = false,
    val ccs2Only: Boolean = false,
    val fastOnly: Boolean = false
) {

    val hasAvailability: Boolean
        get() = stations.any { it.available != null }

    val hasConnectorInfo: Boolean
        get() = stations.any { it.connector != null }

    val hasPowerInfo: Boolean
        get() = stations.any { it.powerKw != null }

    val filteredStations: List<ChargingStation>
        get() = stations.filter { station ->

            (!availableOnly || station.available == true) &&
                    (!ccs2Only || station.connector == "CCS2") &&
                    (!fastOnly || (station.powerKw ?: 0) >= 50)
        }
}

class ChargersViewModel(
    private val repository: ChargingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ChargersUiState()
    )

    val uiState = _uiState.asStateFlow()

    init {
        refreshStations()
    }

    // Consultar estaciones desde el Repository.
    fun refreshStations() {

        if (_uiState.value.isLoading) return

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {

            try {

                val result = repository.fetchStations()

                _uiState.update {
                    it.copy(
                        stations = result,
                        errorMessage = null
                    )
                }

            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: Exception) {

                // Conservar los resultados anteriores
                // cuando falla la conexión.
                _uiState.update {
                    it.copy(
                        errorMessage =
                            "No se pudieron actualizar las " +
                                    "estaciones. Comprueba tu conexión."
                    )
                }

            } finally {

                _uiState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    // Filtro de estaciones disponibles.
    fun toggleAvailableOnly() {

        _uiState.update { state ->

            if (state.hasAvailability) {
                state.copy(
                    availableOnly = !state.availableOnly
                )
            } else {
                state
            }
        }
    }

    // Filtro CCS2.
    fun toggleCcs2Only() {

        _uiState.update { state ->

            if (state.hasConnectorInfo) {
                state.copy(
                    ccs2Only = !state.ccs2Only
                )
            } else {
                state
            }
        }
    }

    // Filtro de carga rápida.
    fun toggleFastOnly() {

        _uiState.update { state ->

            if (state.hasPowerInfo) {
                state.copy(
                    fastOnly = !state.fastOnly
                )
            } else {
                state
            }
        }
    }
}

// Inyección manual del Repository.
class ChargersViewModelFactory(
    private val repository: ChargingRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                ChargersViewModel::class.java
            )
        ) {

            @Suppress("UNCHECKED_CAST")
            return ChargersViewModel(
                repository = repository
            ) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido"
        )
    }
}
