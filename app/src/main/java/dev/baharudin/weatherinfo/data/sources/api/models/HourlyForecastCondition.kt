package dev.baharudin.weatherinfo.data.sources.api.models

import com.google.gson.annotations.SerializedName
import dev.baharudin.weatherinfo.domain.entities.Temperature
import java.time.Instant
import java.util.Date


data class HourlyForecastCondition(
    @SerializedName("dt") val dt: Long,
    @SerializedName("main") val main: Main,
    @SerializedName("weather") val weather: ArrayList<WeatherResponse> = arrayListOf(),
    @SerializedName("wind") val wind: Wind? = Wind(),
    @SerializedName("visibility") val visibility: Int? = null,
    @SerializedName("pop") val pop: Double? = null,//
    @SerializedName("sys") val sys: Sys,
    @SerializedName("dt_txt") val dtTxt: String? = null//
) {
    companion object {
        private const val TAG = "HourlyForecastCondition"
    }

    fun getDate(): Date = Date.from(Instant.ofEpochMilli(dt * 1000))

    fun getTemperature(): Temperature {
        return main.toEntity()
    }

    fun getWeatherList() = weather.map { it.toEntity() }
}