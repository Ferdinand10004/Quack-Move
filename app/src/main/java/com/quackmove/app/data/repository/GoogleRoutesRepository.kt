package com.quackmove.app.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

// Información de una ruta calculada.
data class RouteSummary(
    val origin: String,
    val destination: String,
    val distanceKm: Double,
    val drivingMinutes: Int
)

// Implementación que consulta nuestro servidor.
object GoogleRoutesRepository : RoutesRepository {

    override suspend fun fetchRoute(): RouteSummary =
        withContext(Dispatchers.IO) {

            val connection = URL(
                "http://127.0.0.1:8765/route"
            ).openConnection() as HttpURLConnection

            try {
                connection.requestMethod = "GET"
                connection.connectTimeout = 10000
                connection.readTimeout = 30000

                if (connection.responseCode != 200) {
                    throw IOException(
                        "Error HTTP ${connection.responseCode}"
                    )
                }

                val response = connection.inputStream
                    .bufferedReader(Charsets.UTF_8)
                    .use { it.readText() }

                val json = JSONObject(response)

                RouteSummary(
                    origin = json.getString("origen"),
                    destination = json.getString("destino"),
                    distanceKm = json.getDouble("distanciaKm"),
                    drivingMinutes = json.getInt(
                        "tiempoConduccionMin"
                    )
                )

            } finally {
                connection.disconnect()
            }
        }
}
