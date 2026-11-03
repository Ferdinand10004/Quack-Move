package com.quackmove.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Representa un viaje guardado en el teléfono.
@Entity(tableName = "local_trips")
data class LocalTripEntity(

    @PrimaryKey
    val id: String,

    val origin: String,

    val destination: String,

    val distanceKm: Double,

    val drivingMinutes: Int,

    val departureBatteryPercent: Double,

    val savedAtMillis: Long = System.currentTimeMillis()
)
