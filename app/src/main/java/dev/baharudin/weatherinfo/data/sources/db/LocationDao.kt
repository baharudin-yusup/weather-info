package dev.baharudin.weatherinfo.data.sources.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.baharudin.weatherinfo.data.sources.db.models.LocationDBEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations")
    fun getAllSavedLocation(): Flow<List<LocationDBEntity>>

    @Query("SELECT * FROM locations WHERE city = :city AND state = :state AND country = :country LIMIT 1")
    fun getSavedLocation(city: String, state: String, country: String): Flow<List<LocationDBEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location: LocationDBEntity)

    @Query("DELETE FROM locations WHERE city = :city AND state = :state AND country = :country")
    fun delete(city: String, state: String, country: String)
}