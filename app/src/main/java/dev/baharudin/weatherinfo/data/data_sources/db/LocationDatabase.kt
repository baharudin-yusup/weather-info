package dev.baharudin.weatherinfo.data.data_sources.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.baharudin.weatherinfo.data.data_sources.db.models.LocationDBEntity

@Database(entities = [LocationDBEntity::class], version = 1)
abstract class LocationDatabase : RoomDatabase(){
    companion object {
        const val NAME = "location_database"
    }
    abstract fun locationDao(): LocationDao
}