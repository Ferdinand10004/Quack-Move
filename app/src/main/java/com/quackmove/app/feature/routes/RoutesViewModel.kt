
package com.quackmove.app.feature.routes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quackmove.app.data.repository.GoogleRoutesRepository
import com.quackmove.app.data.repository.RouteSummary
import com.quackmove.app.domain.BatteryEstimate
import com.quackmove.app.domain.BatteryEstimator
import kotlinx.coroutines.launch

class RoutesViewModel : ViewModel() {

    // Información recibida de Google Routes
    var route by mutableStateOf<RouteSummary?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Datos editables del vehículo
    var batteryCapacity by mutableStateOf("60")
        private set

    var consumption by mutableStateOf("18")
        private set

    var departureBattery by mutableStateOf("80")
        private set

    var minimumReserve by mutableStateOf("10")
        private set

    // Resultado del cálculo
    var batteryEstimate by mutableStateOf<BatteryEstimate?>(null)
        private set

    var validationError by mutableStateOf<String?>(null)
        private set

    init {
        refreshRoute()
    }

    // Consultar la ruta a Google
    fun refreshRoute() {
        if (isLoading) return

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            route = null
            batteryEstimate = null

            try {
                route = GoogleRoutesRepository.fetchRoute()
                calculateBattery()
            } catch (exception: Exception) {
                errorMessage =
                    "No se pudo calcular la ruta: " +
                            (exception.message ?: "Error desconocido")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateBatteryCapacity(value: String) {
        batteryCapacity = value
        calculateBattery()
    }

    fun updateConsumption(value: String) {
        consumption = value
        calculateBattery()
    }

    fun updateDepartureBattery(value: String) {
        departureBattery = value
        calculateBattery()
    }

    fun updateMinimumReserve(value: String) {
        minimumReserve = value
        calculateBattery()
    }

    private fun parseNumber(value: String): Double? {
        return value.trim().replace(",", ".").toDoubleOrNull()
    }

    // Calcular la autonomía con los datos del vehículo
    private fun calculateBattery() {

        val currentRoute = route ?: return

        val capacity = parseNumber(batteryCapacity)
        val energyConsumption = parseNumber(consumption)
        val departure = parseNumber(departureBattery)
        val reserve = parseNumber(minimumReserve)

        batteryEstimate = null

        if (
            capacity == null ||
            energyConsumption == null ||
            departure == null ||
            reserve == null
        ) {
            validationError = "Introduce valores numéricos válidos."
            return
        }

        if (
            !capacity.isFinite() ||
            !energyConsumption.isFinite() ||
            capacity <= 0 ||
            energyConsumption <= 0 ||
            departure !in 0.0..100.0 ||
            reserve !in 0.0..100.0
        ) {
            validationError =
                "Revisa los valores. La capacidad y el consumo deben " +
                        "ser positivos, y los porcentajes deben estar entre 0 y 100."
            return
        }

        try {
            batteryEstimate = BatteryEstimator.calculate(
                distanceKm = currentRoute.distanceKm,
                usableBatteryKwh = capacity,
                consumptionKwhPer100Km = energyConsumption,
                departurePercent = departure,
                minimumArrivalPercent = reserve
            )

            validationError = null

        } catch (exception: IllegalArgumentException) {
            validationError = "Los datos del vehículo no son válidos."
        }
    }
}
