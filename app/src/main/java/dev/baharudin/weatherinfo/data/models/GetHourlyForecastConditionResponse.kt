package dev.baharudin.weatherinfo.data.models

import com.google.gson.annotations.SerializedName
import dev.baharudin.weatherinfo.domain.entities.Condition


data class GetHourlyForecastConditionResponse(
    @SerializedName("cod") var cod: String? = null,
    @SerializedName("message") var message: Int? = null,
    @SerializedName("cnt") var cnt: Int? = null,
    @SerializedName("list") var hourlyCondition: ArrayList<HourlyCondition> = arrayListOf(),
    @SerializedName("city") var city: City
) {
    fun getConditions() = hourlyCondition.map {
        Condition(
            date = it.getDate(),
            temperature = it.getTemperature(),
            location = city.getLocation(),
            weathers = it.getWeatherList()
        )
    }
}