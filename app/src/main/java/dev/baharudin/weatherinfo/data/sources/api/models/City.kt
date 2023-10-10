package dev.baharudin.weatherinfo.data.sources.api.models

import com.google.gson.annotations.SerializedName
import dev.baharudin.weatherinfo.domain.entities.Location


data class City(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String,
    @SerializedName("coord") var coord: Coord,
    @SerializedName("country") var country: String,
    @SerializedName("population") var population: Int? = null,
    @SerializedName("timezone") var timezone: Int? = null,
    @SerializedName("sunrise") var sunrise: Int? = null,
    @SerializedName("sunset") var sunset: Int? = null
) {
    fun getLocation() = Location(
        city = name,
        country = country,
        coordinate = coord.toEntity()
    )
}