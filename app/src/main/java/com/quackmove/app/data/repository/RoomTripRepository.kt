package com.quackmove.app.data.repository

import com.quackmove.app.data.local.LocalTripDao
import com.quackmove.app.data.local.LocalTripEntity
import com.quackmove.app.domain.SavedTrip
import com.quackmove.app.domain.TripRepository
import java.util.UUID

// Repository para viajes guardados en el teléfono.
class RoomTripRepository(
    private val dao: LocalTripDao
) : TripRepository {

    override suspend fun saveTrip(
        trip: SavedTrip
    ): String {

        val tripId = trip.id.ifBlank {
            UUID.randomUUID().toString()
        }

        val entity = LocalTripEntity(
            id = tripId,
            origin = trip.origin,
            destination = trip.destination,
            distanceKm = trip.distanceKm,
            drivingMinutes = trip.drivingMinutes,
            departureBatteryPercent =
                trip.departureBatteryPercent
        )

        dao.saveTrip(entity)

        return tripId
    }

    override suspend fun getTrips(): List<SavedTrip> {

        return dao.getTrips().map { entity ->

            SavedTrip(
                id = entity.id,
                origin = entity.origin,
                destination = entity.destination,
                distanceKm = entity.distanceKm,
                drivingMinutes = entity.drivingMinutes,
                departureBatteryPercent =
                    entity.departureBatteryPercent
            )
        }
    }
}
