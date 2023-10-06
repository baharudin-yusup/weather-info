package dev.baharudin.weatherinfo.data.models

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.util.Date


data class HourlyCondition(
    @SerializedName("dt") var dt: Long,
    @SerializedName("main") var main: Main,
    @SerializedName("weather") var weather: ArrayList<WeatherResponse> = arrayListOf(),
    @SerializedName("clouds") var clouds: Clouds? = Clouds(),
    @SerializedName("wind") var wind: Wind? = Wind(),
    @SerializedName("visibility") var visibility: Int? = null,
    @SerializedName("pop") var pop: Double? = null,//
    @SerializedName("sys") var sys: Sys,
    @SerializedName("dt_txt") var dtTxt: String? = null//
) {
    fun getDate(): Date = Date.from(Instant.ofEpochMilli(dt))

    fun getTemperature() = main.toEntity()

    fun getWeatherList() = weather.map { it.toEntity() }
}