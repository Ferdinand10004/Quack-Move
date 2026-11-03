package com.quackmove.app.data.repository

import com.quackmove.app.data.model.ChargingStation

// Contrato para consultar estaciones de carga.
interface ChargingRepository {

    suspend fun fetchStations(): List<ChargingStation>
}
