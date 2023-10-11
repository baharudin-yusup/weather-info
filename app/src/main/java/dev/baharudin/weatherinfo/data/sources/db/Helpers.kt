package dev.baharudin.weatherinfo.data.sources.db

import dev.baharudin.weatherinfo.data.sources.db.models.LocationDBEntity
import dev.baharudin.weatherinfo.domain.entities.Location

fun Location.toDBEntity() = LocationDBEntity(
    city, state, country, coordinate.latitude, coordinate.longitude
)