package com.yotfr.weather.domain.model

import java.time.LocalDateTime

/**
 *[WeatherInfo] contains all information about the weather in a particular place in a convenient format
 */
data class WeatherInfo(
    val completeWeatherData: Map<Int, CompleteWeatherData>,
    val currentWeatherData: HourlyWeatherData,
    val fromCurrentTimeHourlyWeatherData: List<HourlyWeatherData>,
    val todaySunrise: LocalDateTime,
    val todaySunset: LocalDateTime
)
