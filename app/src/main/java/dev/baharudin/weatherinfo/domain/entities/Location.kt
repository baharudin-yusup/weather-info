package dev.baharudin.weatherinfo.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val city: String = "",
    val state: String = "",
    val country: String = "",
    val coordinate: Coordinate
): Parcelable {
    fun getName() = if (state.isNotEmpty()) "$city,$state,$country" else "$city,$country"
}