package dev.baharudin.weatherinfo.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coordinate(
    val longitude: Float,
    val latitude: Float,
): Parcelable
