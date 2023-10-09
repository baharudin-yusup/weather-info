package dev.baharudin.weatherinfo.domain.usecases.location

import dev.baharudin.weatherinfo.core.Resource
import dev.baharudin.weatherinfo.domain.entities.Location
import dev.baharudin.weatherinfo.domain.repositories.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetIsLocationSaved @Inject constructor(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(location: Location): Flow<Resource<Boolean>> =
        locationRepository.isLocationSaved(location).map {
            Resource.Success(it)
        }
}