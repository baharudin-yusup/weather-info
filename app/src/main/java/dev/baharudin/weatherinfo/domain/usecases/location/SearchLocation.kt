package dev.baharudin.weatherinfo.domain.usecases.location

import dev.baharudin.weatherinfo.core.Resource
import dev.baharudin.weatherinfo.domain.entities.Location
import dev.baharudin.weatherinfo.domain.repositories.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchLocation @Inject constructor(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(name: String): Flow<Resource<List<Location>>> = flow {
        try {
            emit(Resource.Loading())
            val result = locationRepository.searchLocation(name)
            emit(Resource.Success(result))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}