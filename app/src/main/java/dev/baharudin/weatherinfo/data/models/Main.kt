package dev.baharudin.weatherinfo.data.models

import com.google.gson.annotations.SerializedName
import dev.baharudin.weatherinfo.domain.entities.Temperature

data class Main(
    @SerializedName("temp") val temp: Float,
    @SerializedName("feels_like") val feelsLike: Double? = null,
    @SerializedName("temp_min") val tempMin: Float,
    @SerializedName("temp_max") val tempMax: Float,
    @SerializedName("pressure") val pressure: Int? = null,
    @SerializedName("humidity") val humidity: Int? = null
) {
    fun toEntity() = Temperature(tempMin, temp, tempMax)
}