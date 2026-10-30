package com.quackmove.app.data.repository

import com.quackmove.app.data.model.ChargingStation

/** Estaciones ficticias de la Entrega 3; no hay conexión a APIs. */
object DemoChargingRepository {
    val demoChargingStations = listOf(
    ChargingStation(
        "Estación de carga A",
        "Zona 10, Guatemala",
        "5.2 km",
        "CCS2",
        50,
        true
    ),
    ChargingStation(
        "Estación de carga B",
        "Zona 13, Guatemala",
        "8.4 km",
        "Tipo 2",
        22,
        true
    ),
    ChargingStation(
        "Estación de carga C",
        "Villa Nueva, Guatemala",
        "3.1 km",
        "CCS2",
        150,
        false
    ),
    ChargingStation(
        "Estación de carga D",
        "Zona 15, Guatemala",
        "12.6 km",
        "CCS2",
        100,
        true
    )
)
}
