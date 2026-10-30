
package com.quackmove.app.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.io.IOException

// Información real de una ruta
data class RouteSummary(
    val origin: String,
    val destination: String,
    val distanceKm: Double,
    val drivingMinutes: Int
)

// Comunicación con nuestro servidor Python
object GoogleRoutesRepository {

    suspend fun fetchRoute(): RouteSummary =
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
                        "Error al obtener la ruta: HTTP ${connection.responseCode}"
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
                    drivingMinutes = json.getInt("tiempoConduccionMin")
                )

            } finally {
                connection.disconnect()
            }
        }
}
