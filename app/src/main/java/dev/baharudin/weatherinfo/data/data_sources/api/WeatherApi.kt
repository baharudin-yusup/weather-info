package dev.baharudin.weatherinfo.data.data_sources.api

import dev.baharudin.weatherinfo.data.models.GetCurrentConditionResponse
import dev.baharudin.weatherinfo.data.models.GetDailyForecastConditionResponse
import dev.baharudin.weatherinfo.data.models.GetHourlyForecastConditionResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getCurrentCondition(
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float,
    ): GetCurrentConditionResponse

    @GET("data/2.5/forecast")
    suspend fun getHourlyForecastCondition(
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float,
    ): GetHourlyForecastConditionResponse

    @GET("data/2.5/forecast/daily")
    suspend fun getDailyForecastCondition(
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float,
    ): GetDailyForecastConditionResponse
}