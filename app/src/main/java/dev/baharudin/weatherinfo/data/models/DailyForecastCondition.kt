package dev.baharudin.weatherinfo.data.models

import android.util.Log
import com.google.gson.annotations.SerializedName
import dev.baharudin.weatherinfo.domain.entities.Temperature
import java.time.Instant
import java.util.Date

data class DailyForecastCondition(
    @SerializedName("dt") val dt: Long,
    @SerializedName("sunrise") val sunrise: Int? = null,
    @SerializedName("sunset") val sunset: Int? = null,
    @SerializedName("temp") val temp: Temp,
    @SerializedName("feels_like") val feelsLike: FeelsLike? = FeelsLike(),
    @SerializedName("pressure") val pressure: Int? = null,
    @SerializedName("humidity") val humidity: Int? = null,
    @SerializedName("weather") val weather: ArrayList<WeatherResponse> = arrayListOf(),
    @SerializedName("speed") val speed: Float? = null,
    @SerializedName("deg") val deg: Int? = null,
    @SerializedName("gust") val gust: Float? = null,
    @SerializedName("clouds") val clouds: Int? = null,
    @SerializedName("pop") val pop: Float? = null,
    @SerializedName("rain") val rain: Float? = null
) {
    fun getDate(): Date = Date.from(Instant.ofEpochMilli(dt * 1000))

    fun getTemperature(): Temperature {
        return temp.toEntity()
    }

    fun getWeatherList() = weather.map { it.toEntity() }
}