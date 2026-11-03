package com.quackmove.app.data.model

data class ChargingStation(
    val name: String,
    val location: String,
    val distance: String = "",
    val connector: String? = null,
    val powerKw: Int? = null,
    val available: Boolean? = null
)
