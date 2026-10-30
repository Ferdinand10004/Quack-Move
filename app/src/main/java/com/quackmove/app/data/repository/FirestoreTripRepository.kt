
package com.quackmove.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.quackmove.app.domain.SavedTrip
import com.quackmove.app.domain.TripRepository
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class FirestoreTripRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : TripRepository {

    private suspend fun getUserId(): String {
        val user = auth.currentUser
            ?: withTimeout(12000) {
                auth.signInAnonymously().await().user
            }

        return user?.uid
            ?: error("No se pudo iniciar sesión.")
    }

    override suspend fun saveTrip(trip: SavedTrip): String {
        val userId = getUserId()

        val data = mapOf(
            "ownerId" to userId,
            "origin" to trip.origin,
            "destination" to trip.destination,
            "distanceKm" to trip.distanceKm,
            "drivingMinutes" to trip.drivingMinutes,
            "departureBatteryPercent" to trip.departureBatteryPercent
        )

        val document = firestore.collection("trips").document()

        // Evitar esperar indefinidamente por confirmación
        withTimeout(12000) {
            document.set(data).await()
        }

        return document.id
    }

    override suspend fun getTrips(): List<SavedTrip> {
        val userId = getUserId()

        val snapshot = withTimeout(12000) {
            firestore.collection("trips")
                .whereEqualTo("ownerId", userId)
                .get()
                .await()
        }

        return snapshot.documents.mapNotNull { document ->
            val origin = document.getString("origin")
                ?: return@mapNotNull null

            val destination = document.getString("destination")
                ?: return@mapNotNull null

            val distance = document.getDouble("distanceKm")
                ?: return@mapNotNull null

            val minutes = document.getLong("drivingMinutes")
                ?: return@mapNotNull null

            val battery = document.getDouble(
                "departureBatteryPercent"
            ) ?: return@mapNotNull null

            SavedTrip(
                id = document.id,
                origin = origin,
                destination = destination,
                distanceKm = distance,
                drivingMinutes = minutes.toInt(),
                departureBatteryPercent = battery
            )
        }
    }
}
