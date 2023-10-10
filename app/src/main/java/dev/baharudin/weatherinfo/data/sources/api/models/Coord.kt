package dev.baharudin.weatherinfo.data.sources.api.models

import com.google.gson.annotations.SerializedName
import dev.baharudin.weatherinfo.domain.entities.Coordinate

data class Coord(
    @SerializedName("lon") val lon: Float,
    @SerializedName("lat") val lat: Float
) {
    fun toEntity() = Coordinate(lon, lat)
}