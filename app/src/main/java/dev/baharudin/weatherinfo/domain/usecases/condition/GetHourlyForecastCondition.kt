package dev.baharudin.weatherinfo.domain.usecases.condition

import dev.baharudin.weatherinfo.common.Resource
import dev.baharudin.weatherinfo.domain.entities.Condition
import dev.baharudin.weatherinfo.domain.entities.Location
import dev.baharudin.weatherinfo.domain.repositories.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetHourlyForecastCondition @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    operator fun invoke(location: Location): Flow<Resource<List<Condition>>> = flow {
        try {
            emit(Resource.Loading())
            val result = weatherRepository.getHourlyForecastCondition(location)
            emit(Resource.Success(result))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}