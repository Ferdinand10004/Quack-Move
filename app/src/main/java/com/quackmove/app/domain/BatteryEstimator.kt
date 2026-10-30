
package com.quackmove.app.domain

import kotlin.math.max

data class BatteryEstimate(
    val energyRequiredKwh: Double,
    val batteryUsedPercent: Double,
    val minimumDeparturePercent: Double,
    val estimatedArrivalPercent: Double?,
    val canCompleteTrip: Boolean,
    val missingEnergyKwh: Double
)

object BatteryEstimator {

    fun calculate(
        distanceKm: Double,
        usableBatteryKwh: Double,
        consumptionKwhPer100Km: Double,
        departurePercent: Double,
        minimumArrivalPercent: Double
    ): BatteryEstimate {

        require(distanceKm >= 0)
        require(usableBatteryKwh > 0)
        require(consumptionKwhPer100Km > 0)
        require(departurePercent in 0.0..100.0)
        require(minimumArrivalPercent in 0.0..100.0)

        // Energía que requiere el recorrido
        val energyRequired =
            distanceKm * consumptionKwhPer100Km / 100.0

        // Porcentaje de batería consumida
        val batteryUsed =
            energyRequired / usableBatteryKwh * 100.0

        // Batería mínima de salida, incluyendo reserva
        val minimumDeparture =
            batteryUsed + minimumArrivalPercent

        // Porcentaje proyectado al finalizar el viaje
        val projectedArrival =
            departurePercent - batteryUsed

        // Verificar si se puede llegar con la reserva
        val canComplete =
            projectedArrival >= minimumArrivalPercent

        // Energía adicional necesaria para respetar la reserva
        val safeEnergyAvailable =
            usableBatteryKwh *
                    max(0.0, departurePercent - minimumArrivalPercent) /
                    100.0

        val missingEnergy =
            max(0.0, energyRequired - safeEnergyAvailable)

        return BatteryEstimate(
            energyRequiredKwh = energyRequired,
            batteryUsedPercent = batteryUsed,
            minimumDeparturePercent = minimumDeparture,
            estimatedArrivalPercent =
                projectedArrival.takeIf { it >= 0.0 },
            canCompleteTrip = canComplete,
            missingEnergyKwh = missingEnergy
        )
    }
}
