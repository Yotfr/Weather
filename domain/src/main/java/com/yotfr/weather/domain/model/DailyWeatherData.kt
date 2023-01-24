package com.yotfr.weather.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class DailyWeatherData(
    val time: LocalDate,
    val weatherType: WeatherType,
    val maxTemperature: Double,
    val minTemperature: Double,
    val sunrise: LocalDateTime,
    val sunset: LocalDateTime
)
