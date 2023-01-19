package com.yotfr.weather.domain.model

import java.time.LocalDateTime

/**
 * @param[detailedHourlyWeatherDataPerDay] map the day index of type Int to list of [HourlyWeatherData], which
 * contains information of that day weather per hour
 * @param[currentHourlyWeatherData] contains information of weather for the current hour
 */
data class WeatherInfo(
    val completeWeatherData: Map<Int, CompleteWeatherData>,
    val currentWeatherData: HourlyWeatherData,
    val fromCurrentTimeHourlyWeatherData: List<HourlyWeatherData>,
    val todaySunrise: LocalDateTime,
    val todaySunset: LocalDateTime
)
