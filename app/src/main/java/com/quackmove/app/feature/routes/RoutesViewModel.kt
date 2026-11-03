package com.quackmove.app.feature.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.quackmove.app.data.repository.RouteSummary
import com.quackmove.app.data.repository.RoutesRepository
import com.quackmove.app.domain.BatteryEstimate
import com.quackmove.app.domain.BatteryEstimator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// =====================================
// ESTADO DEL PLANIFICADOR
// =====================================

data class RoutesUiState(
    val route: RouteSummary? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    // Datos provisionales del vehículo
    val batteryCapacity: String = "60",
    val consumption: String = "18",
    val departureBattery: String = "80",
    val minimumReserve: String = "10",

    // Resultado del cálculo de autonomía
    val batteryEstimate: BatteryEstimate? = null,
    val validationError: String? = null
)

// =====================================
// VIEWMODEL
// =====================================

class RoutesViewModel(
    private val routesRepository: RoutesRepository
) : ViewModel() {

    // Estado privado
    private val _uiState = MutableStateFlow(
        RoutesUiState()
    )

    // Estado público para la pantalla
    val uiState = _uiState.asStateFlow()

    init {
        refreshRoute()
    }

    // =====================================
    // CONSULTAR O ACTUALIZAR LA RUTA
    // =====================================

    fun refreshRoute() {

        // Evitar consultas simultáneas
        if (_uiState.value.isLoading) return

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {

            try {

                // Consultar mediante Repository
                val result = routesRepository.fetchRoute()

                // Actualizar solamente cuando la consulta
                // termina correctamente
                _uiState.update {
                    it.copy(
                        route = result,
                        errorMessage = null
                    )
                }

                // Recalcular la autonomía
                calculateBattery()

            } catch (exception: CancellationException) {

                // Respetar la cancelación de corrutinas
                throw exception

            } catch (exception: Exception) {

                // M10: conservar la última ruta válida
                // y su cálculo si falla la actualización
                _uiState.update { state ->

                    state.copy(
                        errorMessage = if (state.route != null) {

                            "No se pudo actualizar la ruta. " +
                                    "Se conservan los datos anteriores."

                        } else {

                            "No se pudo cargar la ruta. " +
                                    "Comprueba Internet y el servidor."
                        }
                    )
                }

            } finally {

                // Terminar el estado de carga
                _uiState.update {
                    it.copy(
                        isLoading = false
                    )
                }
            }
        }
    }

    // =====================================
    // ACTUALIZAR CAPACIDAD DE BATERÍA
    // =====================================

    fun updateBatteryCapacity(value: String) {

        _uiState.update {
            it.copy(
                batteryCapacity = value
            )
        }

        calculateBattery()
    }

    // =====================================
    // ACTUALIZAR CONSUMO DEL VEHÍCULO
    // =====================================

    fun updateConsumption(value: String) {

        _uiState.update {
            it.copy(
                consumption = value
            )
        }

        calculateBattery()
    }

    // =====================================
    // ACTUALIZAR BATERÍA DE SALIDA
    // =====================================

    fun updateDepartureBattery(value: String) {

        _uiState.update {
            it.copy(
                departureBattery = value
            )
        }

        calculateBattery()
    }

    // =====================================
    // ACTUALIZAR RESERVA MÍNIMA
    // =====================================

    fun updateMinimumReserve(value: String) {

        _uiState.update {
            it.copy(
                minimumReserve = value
            )
        }

        calculateBattery()
    }

    // =====================================
    // CONVERSIÓN DE VALORES
    // =====================================

    private fun parseNumber(value: String): Double? {

        return value.trim()
            .replace(",", ".")
            .toDoubleOrNull()
    }

    // =====================================
    // CÁLCULO DE AUTONOMÍA
    // =====================================

    private fun calculateBattery() {

        val state = _uiState.value

        // Necesitamos una ruta para calcular
        val currentRoute = state.route

        if (currentRoute == null) {

            _uiState.update {
                it.copy(
                    batteryEstimate = null,
                    validationError = null
                )
            }

            return
        }

        // Convertir datos del vehículo
        val capacity = parseNumber(
            state.batteryCapacity
        )

        val energyConsumption = parseNumber(
            state.consumption
        )

        val departure = parseNumber(
            state.departureBattery
        )

        val reserve = parseNumber(
            state.minimumReserve
        )

        // =====================================
        // VALIDACIONES
        // =====================================

        if (
            capacity == null ||
            energyConsumption == null ||
            departure == null ||
            reserve == null
        ) {

            _uiState.update {
                it.copy(
                    batteryEstimate = null,
                    validationError =
                        "Introduce valores numéricos válidos."
                )
            }

            return
        }

        if (
            !capacity.isFinite() ||
            !energyConsumption.isFinite() ||
            !departure.isFinite() ||
            !reserve.isFinite() ||
            capacity <= 0 ||
            energyConsumption <= 0 ||
            departure !in 0.0..100.0 ||
            reserve !in 0.0..100.0
        ) {

            _uiState.update {
                it.copy(
                    batteryEstimate = null,
                    validationError =
                        "Revisa los datos del vehículo. " +
                                "Los porcentajes deben estar " +
                                "entre 0 y 100."
                )
            }

            return
        }

        // =====================================
        // EJECUTAR CÁLCULO DE BATERÍA
        // =====================================

        try {

            val estimate = BatteryEstimator.calculate(
                distanceKm = currentRoute.distanceKm,
                usableBatteryKwh = capacity,
                consumptionKwhPer100Km =
                    energyConsumption,
                departurePercent = departure,
                minimumArrivalPercent = reserve
            )

            _uiState.update {
                it.copy(
                    batteryEstimate = estimate,
                    validationError = null
                )
            }

        } catch (exception: IllegalArgumentException) {

            _uiState.update {
                it.copy(
                    batteryEstimate = null,
                    validationError =
                        "Los datos del vehículo no son válidos."
                )
            }
        }
    }
}

// =====================================
// FACTORY - INYECCIÓN MANUAL
// =====================================

class RoutesViewModelFactory(
    private val repository: RoutesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                RoutesViewModel::class.java
            )
        ) {

            @Suppress("UNCHECKED_CAST")
            return RoutesViewModel(
                routesRepository = repository
            ) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido"
        )
    }
}
