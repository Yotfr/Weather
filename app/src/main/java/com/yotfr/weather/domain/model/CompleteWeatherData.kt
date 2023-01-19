package com.yotfr.weather.domain.model

data class CompleteWeatherData(
    val hourlyWeatherData: List<HourlyWeatherData>,
    val dailyWeatherData: DailyWeatherData
)