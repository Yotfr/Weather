package com.yotfr.weather.data.datasource.remote.dto

import com.squareup.moshi.Json

data class DailyWeatherDataDto(
    @field:Json(name = "time")
    val time: List<String>,
    @field:Json(name = "weathercode")
    val weatherCodes: List<Int>,
    @field:Json(name = "temperature_2m_max")
    val maxTemperature: List<Double>,
    @field:Json(name = "temperature_2m_min")
    val minTemperature: List<Double>,
    val sunrise: List<String>,
    val sunset: List<String>
)