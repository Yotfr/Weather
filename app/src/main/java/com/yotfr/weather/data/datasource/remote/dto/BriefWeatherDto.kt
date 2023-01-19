package com.yotfr.weather.data.datasource.remote.dto

import com.squareup.moshi.Json

data class BriefWeatherDto(
    @field:Json(name = "current_weather")
    val currentWeather: BriefWeatherDataDto
)
