package dev.baharudin.weatherinfo.data.repositories

import dev.baharudin.weatherinfo.data.data_sources.api.LocationApi
import dev.baharudin.weatherinfo.data.data_sources.db.LocationDao
import dev.baharudin.weatherinfo.data.data_sources.db.models.toDBEntity
import dev.baharudin.weatherinfo.domain.entities.Location
import dev.baharudin.weatherinfo.domain.repositories.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationDao: LocationDao,
    private val locationApi: LocationApi,
) :
    LocationRepository {
    override suspend fun searchLocation(name: String): List<Location> {
        val response = locationApi.searchLocation(name)
        return response.map { it.toEntity() }
    }

    override suspend fun saveLocation(location: Location) {
        locationDao.insertLocation(location.toDBEntity())
    }

    override suspend fun removeSavedLocation(location: Location) {
        locationDao.delete(location.toDBEntity())
    }

    override fun getSavedLocation(): Flow<List<Location>> {
        return locationDao.getAllSavedLocation()
            .map { dbEntities -> dbEntities.map { it.toEntity() } }
    }
}