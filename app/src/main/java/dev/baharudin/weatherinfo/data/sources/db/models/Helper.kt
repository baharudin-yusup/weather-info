package dev.baharudin.weatherinfo.data.sources.db.models

import dev.baharudin.weatherinfo.domain.entities.Location

fun Location.toDBEntity() = LocationDBEntity(
    city, state, country, coordinate.latitude, coordinate.longitude
)