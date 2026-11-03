package com.quackmove.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BatteryEstimatorTest {

    // Caso 1: La batería alcanza para viajar.
    @Test
    fun batteryEnoughForTrip() {

        val result = BatteryEstimator.calculate(
            distanceKm = 100.0,
            usableBatteryKwh = 60.0,
            consumptionKwhPer100Km = 18.0,
            departurePercent = 80.0,
            minimumArrivalPercent = 10.0
        )

        assertEquals(
            18.0,
            result.energyRequiredKwh,
            0.001
        )

        assertEquals(
            30.0,
            result.batteryUsedPercent,
            0.001
        )

        val arrivalPercent = checkNotNull(result.estimatedArrivalPercent)

        assertEquals(
            50.0,
            arrivalPercent,
            0.001
        )

        assertTrue(result.canCompleteTrip)
        assertEquals(0.0, result.missingEnergyKwh, 0.001)
    }

    // Caso 2: La batería no alcanza.
    @Test
    fun batteryNotEnoughForLongTrip() {

        val result = BatteryEstimator.calculate(
            distanceKm = 500.0,
            usableBatteryKwh = 60.0,
            consumptionKwhPer100Km = 18.0,
            departurePercent = 80.0,
            minimumArrivalPercent = 10.0
        )

        assertFalse(result.canCompleteTrip)

        assertEquals(
            90.0,
            result.energyRequiredKwh,
            0.001
        )

        assertEquals(
            48.0,
            result.missingEnergyKwh,
            0.001
        )

        assertNull(result.estimatedArrivalPercent)
    }

    // Caso 3: Un recorrido de cero kilómetros.
    @Test
    fun zeroDistanceRequiresNoEnergy() {

        val result = BatteryEstimator.calculate(
            distanceKm = 0.0,
            usableBatteryKwh = 60.0,
            consumptionKwhPer100Km = 18.0,
            departurePercent = 80.0,
            minimumArrivalPercent = 10.0
        )

        assertEquals(
            0.0,
            result.energyRequiredKwh,
            0.001
        )

        assertTrue(result.canCompleteTrip)
    }

    // Caso 4: Capacidad inválida.
    @Test(expected = IllegalArgumentException::class)
    fun zeroBatteryCapacityIsRejected() {

        BatteryEstimator.calculate(
            distanceKm = 100.0,
            usableBatteryKwh = 0.0,
            consumptionKwhPer100Km = 18.0,
            departurePercent = 80.0,
            minimumArrivalPercent = 10.0
        )
    }

    // Caso 5: Porcentaje inválido.
    @Test(expected = IllegalArgumentException::class)
    fun invalidBatteryPercentageIsRejected() {

        BatteryEstimator.calculate(
            distanceKm = 100.0,
            usableBatteryKwh = 60.0,
            consumptionKwhPer100Km = 18.0,
            departurePercent = 110.0,
            minimumArrivalPercent = 10.0
        )
    }
}
