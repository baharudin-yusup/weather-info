package dev.baharudin.weatherinfo.domain.usecases.location

import dev.baharudin.weatherinfo.common.Resource
import dev.baharudin.weatherinfo.domain.entities.Condition
import dev.baharudin.weatherinfo.domain.entities.Location
import dev.baharudin.weatherinfo.domain.repositories.LocationRepository
import dev.baharudin.weatherinfo.domain.repositories.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetSavedLocation @Inject constructor(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(): Flow<Resource<List<Location>>> =
        locationRepository.getSavedLocation().map { Resource.Success(it) }
}