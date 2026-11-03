package com.quackmove.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalTripDao {

    // Guardar o actualizar un viaje.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTrip(trip: LocalTripEntity)

    // Guardar varios viajes.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTrips(trips: List<LocalTripEntity>)

    // Consultar el historial local.
    @Query(
        "SELECT * FROM local_trips ORDER BY savedAtMillis DESC"
    )
    suspend fun getTrips(): List<LocalTripEntity>

    // Observar cambios en los viajes.
    @Query(
        "SELECT * FROM local_trips ORDER BY savedAtMillis DESC"
    )
    fun observeTrips(): Flow<List<LocalTripEntity>>

    // Buscar un viaje específico.
    @Query(
        "SELECT * FROM local_trips WHERE id = :tripId LIMIT 1"
    )
    suspend fun getTripById(
        tripId: String
    ): LocalTripEntity?

    // Eliminar un viaje específico.
    @Query(
        "DELETE FROM local_trips WHERE id = :tripId"
    )
    suspend fun deleteTrip(tripId: String)
}
