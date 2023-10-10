package dev.baharudin.weatherinfo.data.sources.api.models

import com.google.gson.annotations.SerializedName
import dev.baharudin.weatherinfo.domain.entities.Weather

data class WeatherResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("icon") val icon: String
) {
    fun toEntity() = Weather(id, icon)
}