package dev.baharudin.weatherinfo.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Weather(
    val id: Int,
    val icon: String,
): Parcelable