package com.yotfr.weather.domain.model

/**
 * [CompleteWeatherData] contains complete information about the weather in a particular place
 */
data class CompleteWeatherData(
    val hourlyWeatherData: List<HourlyWeatherData>,
    val dailyWeatherData: DailyWeatherData
)