package dev.baharudin.weatherinfo.data.data_sources.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.baharudin.weatherinfo.data.data_sources.db.models.LocationDBEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations")
    fun getAllSavedLocation(): Flow<List<LocationDBEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location: LocationDBEntity)

    @Delete
    fun delete(location: LocationDBEntity)
}