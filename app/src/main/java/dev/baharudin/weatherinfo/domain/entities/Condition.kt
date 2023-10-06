package dev.baharudin.weatherinfo.domain.entities

import java.util.Date

data class Condition(
    val date: Date? = null,
    val temperature: Temperature? = null,
    val location: Location,
    val weathers: List<Weather>? = null
)
