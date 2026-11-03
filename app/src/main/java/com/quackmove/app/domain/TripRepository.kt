package com.quackmove.app.domain

interface TripRepository {

    suspend fun saveTrip(trip: SavedTrip): String

    suspend fun getTrips(): List<SavedTrip>
}
