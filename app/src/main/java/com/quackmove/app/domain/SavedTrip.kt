
package com.quackmove.app.domain

data class SavedTrip(
    val id: String = "",
    val origin: String,
    val destination: String,
    val distanceKm: Double,
    val drivingMinutes: Int,
    val departureBatteryPercent: Double
)
