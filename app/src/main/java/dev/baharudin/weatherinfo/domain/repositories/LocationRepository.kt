package dev.baharudin.weatherinfo.domain.repositories

import dev.baharudin.weatherinfo.domain.entities.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun searchLocation(name: String): List<Location>
    suspend fun saveLocation(location: Location)
    suspend fun removeSavedLocation(location: Location)
    fun getSavedLocation(): Flow<List<Location>>
}