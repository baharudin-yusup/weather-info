package dev.baharudin.weatherinfo.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Temperature(
    val min: Float,
    val current: Float?,
    val max: Float,
) : Parcelable
