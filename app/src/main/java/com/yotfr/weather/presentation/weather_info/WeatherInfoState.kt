package com.yotfr.weather.presentation.weather_info

import com.yotfr.weather.domain.model.weather.WeatherInfo

data class WeatherInfoState(
    val isLoading: Boolean = false,
    val weatherInfo: WeatherInfo? = null
)