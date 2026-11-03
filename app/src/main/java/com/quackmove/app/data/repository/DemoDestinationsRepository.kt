package com.quackmove.app.data.repository

import com.quackmove.app.data.model.RecentDestination

/** Datos de demostración de la Entrega 3. No son búsquedas GPS reales. */
object DemoDestinationsRepository {
    val demoRecentDestinations = listOf(
        RecentDestination(
            name = "Universidad del Valle de Guatemala",
            address = "18 Avenida 11-95, Zona 15, Ciudad de Guatemala"
        ),
        RecentDestination(
            name = "Puerta 8 UVG",
            address = "Acceso a la Universidad del Valle de Guatemala"
        ),
        RecentDestination(
            name = "Campo Marte GUA022",
            address = "3a Calle, Zona 10, Guatemala"
        ),
        RecentDestination(
            name = "Pollo Campero, Las Américas",
            address = "Zona 13, Guatemala"
        ),
        RecentDestination(
            name = "Parque Erick Barrondo García",
            address = "Zona 7, Guatemala"
        )
    )

    val searchablePlaces: List<Pair<String, String>> = listOf(
        "Casa" to "5 Calle, Fuentes del Valle 4",
        "Trabajo" to "Agregar dirección",
        "Universidad del Valle de Guatemala" to "18 Avenida 11-95, Zona 15, Guatemala",
        "Puerta 8 UVG" to "Acceso a la Universidad del Valle de Guatemala",
        "Campo Marte GUA022" to "3a Calle, Zona 10, Guatemala",
        "Pollo Campero, Las Américas" to "Zona 13, Guatemala",
        "Parque Erick Barrondo García" to "Zona 7, Guatemala"
    )
}
