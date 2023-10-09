package dev.baharudin.weatherinfo.data.models

import com.google.gson.annotations.SerializedName
import dev.baharudin.weatherinfo.domain.entities.Condition


data class GetHourlyForecastConditionResponse(
    @SerializedName("cod") val cod: String? = null,
    @SerializedName("message") val message: Float? = null,
    @SerializedName("cnt") val cnt: Int? = null,
    @SerializedName("list") val hourlyForecastCondition: ArrayList<HourlyForecastCondition> = arrayListOf(),
    @SerializedName("city") val city: City
) {
    fun getConditions() = hourlyForecastCondition.map {
        Condition(
            date = it.getDate(),
            temperature = it.getTemperature(),
            location = city.getLocation(),
            weathers = it.getWeatherList()
        )
    }
}