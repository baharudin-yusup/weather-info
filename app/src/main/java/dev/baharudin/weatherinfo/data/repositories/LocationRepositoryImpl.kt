package dev.baharudin.weatherinfo.data.repositories

import dev.baharudin.weatherinfo.data.sources.api.LocationApi
import dev.baharudin.weatherinfo.data.sources.db.LocationDao
import dev.baharudin.weatherinfo.data.sources.db.toDBEntity
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
        locationDao.delete(location.city, location.state, location.country)
    }

    override fun isLocationSaved(location: Location): Flow<Boolean> {
        return locationDao.getSavedLocation(location.city, location.state, location.country).map {
            it.isNotEmpty()
        }
    }

    override fun getSavedLocation(): Flow<List<Location>> {
        return locationDao.getAllSavedLocation()
            .map { dbEntities -> dbEntities.reversed().map { it.toEntity() } }
    }
}