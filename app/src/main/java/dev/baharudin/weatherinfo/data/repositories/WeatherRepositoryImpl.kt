package dev.baharudin.weatherinfo.data.repositories

import dev.baharudin.weatherinfo.data.data_sources.api.WeatherApi
import dev.baharudin.weatherinfo.domain.entities.Condition
import dev.baharudin.weatherinfo.domain.entities.Location
import dev.baharudin.weatherinfo.domain.repositories.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val weatherApi: WeatherApi) :
    WeatherRepository {
    override suspend fun getCurrentCondition(location: Location): Condition {
        val response = weatherApi.getCurrentCondition(location.getName())
        return response.getCondition()
    }

    override suspend fun getHourlyForecastCondition(location: Location): List<Condition> {
        val response = weatherApi.getHourlyForecastCondition(location.getName())
        return response.getConditions()
    }

    override suspend fun getDailyForecastCondition(location: Location): List<Condition> {
        TODO("Not yet implemented")
    }
}