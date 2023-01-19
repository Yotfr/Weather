package com.yotfr.weather.data.datasource.remote.dto

import com.squareup.moshi.Json

data class BriefWeatherDataDto(
    val temperature: Double,
    @field:Json(name = "weathercode")
    val weatherCode: Int
)