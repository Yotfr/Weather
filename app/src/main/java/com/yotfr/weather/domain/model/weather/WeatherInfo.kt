package com.yotfr.weather.domain.model.weather

/**
 * @param[detailedWeatherDataPerDay] map the day index of type Int to list of [WeatherData], which
 * contains information of that day weather per hour
 * @param[currentWeatherData] contains information of weather for the current hour
 * @param[briefWeatherDataPerDay] contains brief information of [detailedWeatherDataPerDay]
 */
data class WeatherInfo(
    val detailedWeatherDataPerDay: Map<Int, List<WeatherData>>,
    val currentWeatherData: WeatherData?,
    val briefWeatherDataPerDay: Map<Int, List<WeatherData>>
)
