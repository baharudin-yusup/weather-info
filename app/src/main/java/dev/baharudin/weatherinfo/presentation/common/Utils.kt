package dev.baharudin.weatherinfo.presentation.common

import dev.baharudin.weatherinfo.data.const.ApiConstant
import dev.baharudin.weatherinfo.domain.entities.Condition


fun Condition.getWeatherIconUrl(): String? {
    return if (this.weathers.isNullOrEmpty()) {
        null
    } else {
        "${ApiConstant.WEATHER_ICON_BASE_URL}${this.weathers.first().icon}@4x.png"
    }
}
