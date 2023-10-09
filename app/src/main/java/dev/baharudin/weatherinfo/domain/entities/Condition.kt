package dev.baharudin.weatherinfo.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Condition(
    val date: Date? = null,
    val temperature: Temperature? = null,
    val location: Location,
    val weathers: List<Weather>? = null
) : Parcelable