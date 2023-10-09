package dev.baharudin.weatherinfo.presentation.common

import dev.baharudin.weatherinfo.domain.entities.Condition


fun Condition.getWeatherIconUrl(bigSize: Boolean = false): String? {
    return if (this.weathers.isNullOrEmpty()) {
        null
    } else {
        "https://openweathermap.org/img/wn/${this.weathers.first().icon}@${if (bigSize) "4" else "2"}x.png"
    }
}
