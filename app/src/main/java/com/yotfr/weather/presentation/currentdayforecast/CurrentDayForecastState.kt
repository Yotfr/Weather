package com.yotfr.weather.presentation.currentdayforecast

import androidx.annotation.DrawableRes
import com.yotfr.weather.domain.model.HourlyWeatherData

data class CurrentDayForecastState(
    val isLoading: Boolean = false,
    val currentTime: String = "",
    val toolbarTitle: String = "",
    @DrawableRes val currentWeatherTypeIconRes: Int ? = null,
    val currentWeatherTypeDescription: String = "",
    val currentTemperature: String = "",
    val currentApparentTemperature: String = "",
    val currentPressure: String = "",
    val currentHumidity: String = "",
    val currentWindSpeed: String = "",
    val sunriseTime: String = "",
    val sunsetTime: String = "",
    val hourlyWeatherList: List<HourlyWeatherData> ? = emptyList()
)
