package dev.baharudin.weatherinfo.domain.entities

data class Location(
    val city: String,
    val state: String = "",
    val country: String,
    val coordinate: Coordinate
) {
    fun getName() = if (state.isNotEmpty()) "$city,$state,$country" else "$city,$country"
}