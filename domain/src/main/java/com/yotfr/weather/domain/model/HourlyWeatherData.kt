package com.yotfr.weather.domain.model

import java.time.LocalDateTime

/**
 * [HourlyWeatherData] contains information about the weather on a particular hour
 * @param [time] contains date and time
 */
data class HourlyWeatherData(
    val time: LocalDateTime,
    val temperature: Double,
    val pressure: Double,
    val windSpeed: Double,
    val humidity: Double,
    val weatherType: WeatherType,
    val apparentTemperature: Double
)
