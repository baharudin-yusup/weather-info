package dev.baharudin.weatherinfo.data.models

import com.google.gson.annotations.SerializedName
import dev.baharudin.weatherinfo.domain.entities.Coordinate
import dev.baharudin.weatherinfo.domain.entities.Location

data class LocationResponse(
    @SerializedName("name") val name: String,
    @SerializedName("lat") val lat: Float,
    @SerializedName("lon") val lon: Float,
    @SerializedName("country") val country: String,
    @SerializedName("state") val state: String? = null
) {
    fun toEntity() =
        Location(city = name, state = state ?: "", country = country, coordinate = Coordinate(lon, lat))
}