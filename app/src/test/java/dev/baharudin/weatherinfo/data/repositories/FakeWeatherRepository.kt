package dev.baharudin.weatherinfo.data.repositories

import dev.baharudin.weatherinfo.domain.entities.Condition
import dev.baharudin.weatherinfo.domain.entities.Location
import dev.baharudin.weatherinfo.domain.repositories.WeatherRepository

class FakeWeatherRepository : WeatherRepository {
    private lateinit var _currentCondition: Condition
    private var _currentConditionError: Throwable? = null
    private lateinit var _hourlyCondition: List<Condition>
    private var _hourlyConditionError: Throwable? = null
    private lateinit var _dailyCondition: List<Condition>
    private var _dailyConditionError: Throwable? = null

    fun setReturn(
        currentCondition: Condition? = null,
        hourlyCondition: List<Condition>? = null,
        dailyCondition: List<Condition>? = null
    ) {
        if (currentCondition != null) {
            _currentCondition = currentCondition
        }
        if (hourlyCondition != null) {
            _hourlyCondition = hourlyCondition
        }
        if (dailyCondition != null) {
            _dailyCondition = dailyCondition
        }
    }

    fun setError(
        currentCondition: Throwable? = null,
        hourlyCondition: Throwable? = null,
        dailyCondition: Throwable? = null
    ) {
        if (currentCondition != null) {
            _currentConditionError = currentCondition
        }
        if (hourlyCondition != null) {
            _hourlyConditionError = hourlyCondition
        }
        if (dailyCondition != null) {
            _dailyConditionError = dailyCondition
        }
    }

    override suspend fun getCurrentCondition(location: Location): Condition {
        val error = _currentConditionError
        if (error != null) {
            _currentConditionError = null
            throw error
        }
        return _currentCondition
    }

    override suspend fun getHourlyForecastCondition(location: Location): List<Condition> {
        val error = _hourlyConditionError
        if (error != null) {
            _hourlyConditionError = null
            throw error
        }
        return _hourlyCondition
    }

    override suspend fun getDailyForecastCondition(location: Location): List<Condition> {
        val error = _dailyConditionError
        if (error != null) {
            _dailyConditionError = null
            throw error
        }
        return _dailyCondition
    }
}