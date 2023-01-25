package com.yotfr.weather.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * [DailyWeatherData] contains information about the weather on a particular day
 */
data class DailyWeatherData(
    val date: LocalDate,
    val weatherType: WeatherType,
    val maxTemperature: Double,
    val minTemperature: Double,
    val sunrise: LocalDateTime,
    val sunset: LocalDateTime
)
