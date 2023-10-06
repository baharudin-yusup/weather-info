package dev.baharudin.weatherinfo.domain.repositories

import dev.baharudin.weatherinfo.domain.entities.Condition
import dev.baharudin.weatherinfo.domain.entities.Location

interface WeatherRepository {
    suspend fun getCurrentCondition(location: Location): Condition
    suspend fun getHourlyForecastCondition(location: Location): List<Condition>
    suspend fun getDailyForecastCondition(location: Location): List<Condition>
}