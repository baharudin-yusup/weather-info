package dev.baharudin.weatherinfo.data.sources.api.models

import com.google.gson.annotations.SerializedName
import dev.baharudin.weatherinfo.domain.entities.Condition
import dev.baharudin.weatherinfo.domain.entities.Location
import java.time.Instant
import java.util.Date

data class GetCurrentConditionResponse(
    @SerializedName("coord") val coord: Coord,
    @SerializedName("weather") val weather: ArrayList<WeatherResponse> = arrayListOf(),//
    @SerializedName("base") val base: String? = null,
    @SerializedName("main") val main: Main,//
    @SerializedName("visibility") val visibility: Int? = null,//
    @SerializedName("wind") val wind: Wind? = Wind(),//
    @SerializedName("dt") val dt: Long,//
    @SerializedName("sys") val sys: Sys,//
    @SerializedName("timezone") val timezone: Int? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String = "",
    @SerializedName("cod") val cod: Int? = null
) {
    private fun getDate(): Date = Date.from(Instant.ofEpochMilli(dt))
    private fun getTemperature() = main.toEntity()
    private fun getLocation() = Location(
        city = name,
        country = sys.country,
        coordinate = coord.toEntity()
    )
    private fun getWeatherList() = weather.map { it.toEntity() }

    fun getCondition() = Condition(
        date = getDate(),
        temperature = getTemperature(),
        location = getLocation(),
        weathers = getWeatherList()
    )
}