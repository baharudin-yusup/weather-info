package dev.baharudin.weatherinfo.data.data_sources.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.baharudin.weatherinfo.domain.entities.Coordinate
import dev.baharudin.weatherinfo.domain.entities.Location

@Entity(tableName = "locations")
data class LocationDBEntity(
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "state") val state: String,
    @ColumnInfo(name = "country") val county: String,
    @ColumnInfo(name = "latitude") val latitude: Float,
    @ColumnInfo(name = "longitude") val longitude: Float,
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
) {
    fun toEntity() = Location(city, state, county, Coordinate(longitude, latitude))
}
