package dev.baharudin.weatherinfo.data.models

import com.google.gson.annotations.SerializedName
import dev.baharudin.weatherinfo.domain.entities.Temperature

data class Temp(
    @SerializedName("day") val day: Float? = null,
    @SerializedName("min") val min: Float,
    @SerializedName("max") val max: Float,
    @SerializedName("night") val night: Float? = null,
    @SerializedName("eve") val eve: Float? = null,
    @SerializedName("morn") val morn: Float? = null
) {
    fun toEntity(): Temperature = Temperature(min, null, max)
}