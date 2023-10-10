package dev.baharudin.weatherinfo.data.sources.api.models

import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("type") val type: Int? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("country") val country: String = "",
    @SerializedName("sunrise") val sunrise: Int? = null,
    @SerializedName("sunset") val sunset: Int? = null
)