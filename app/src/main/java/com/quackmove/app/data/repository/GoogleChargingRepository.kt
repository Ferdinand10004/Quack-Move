package com.quackmove.app.data.repository

import com.quackmove.app.data.model.ChargingStation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

// Implementación del Repository con nuestro servidor.
object GoogleChargingRepository : ChargingRepository {

    override suspend fun fetchStations(): List<ChargingStation> =
        withContext(Dispatchers.IO) {

            val connection = URL(
                "http://127.0.0.1:8765/chargers"
            ).openConnection() as HttpURLConnection

            try {
                connection.requestMethod = "GET"
                connection.connectTimeout = 10000
                connection.readTimeout = 25000

                if (connection.responseCode != 200) {
                    throw IOException(
                        "Error HTTP ${connection.responseCode}"
                    )
                }

                val response = connection.inputStream
                    .bufferedReader(Charsets.UTF_8)
                    .use { it.readText() }

                val places = JSONObject(response)
                    .optJSONArray("places")
                    ?: return@withContext emptyList()

                (0 until places.length()).mapNotNull { index ->

                    val place = places.optJSONObject(index)
                        ?: return@mapNotNull null

                    val name = place
                        .optJSONObject("displayName")
                        ?.optString("text")
                        ?.takeIf { it.isNotBlank() }
                        ?: "Estación sin nombre"

                    ChargingStation(
                        name = name,
                        location = place.optString(
                            "formattedAddress",
                            "Dirección desconocida"
                        )
                    )
                }

            } finally {
                connection.disconnect()
            }
        }
}
