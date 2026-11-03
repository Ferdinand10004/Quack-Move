package com.quackmove.app.data.repository

// Contrato para obtener rutas.
// Permite utilizar distintas implementaciones.
interface RoutesRepository {

    suspend fun fetchRoute(): RouteSummary
}
