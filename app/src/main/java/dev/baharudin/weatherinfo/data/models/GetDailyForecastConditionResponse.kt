package dev.baharudin.weatherinfo.data.models

import com.google.gson.annotations.SerializedName
import dev.baharudin.weatherinfo.domain.entities.Condition

data class GetDailyForecastConditionResponse(
    @SerializedName("city") val city: City,
    @SerializedName("cod") val cod: String? = null,
    @SerializedName("message") val message: Double? = null,
    @SerializedName("cnt") val cnt: Int? = null,
    @SerializedName("list") val dailyForecastCondition: ArrayList<DailyForecastCondition> = arrayListOf()
) {
    fun getConditions() = dailyForecastCondition.map {
        Condition(
            date = it.getDate(),
            temperature = it.getTemperature(),
            location = city.getLocation(),
            weathers = it.getWeatherList()
        )
    }
}