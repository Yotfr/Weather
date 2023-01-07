package com.yotfr.weather.domain.model

/**
 * @param[weatherDataPerDay] map the current day index of type Int to list of [WeatherData], which
 * contains information of that day weather per hour
 * @param[currentWeatherData] contains information of weather for the current hour
 */
data class WeatherInfo(
    val weatherDataPerDay: Map<Int, List<WeatherData>>,
    val currentWeatherData: WeatherData?
)
