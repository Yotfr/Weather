package com.yotfr.weather.presentation.weather.weather_info

import androidx.annotation.DrawableRes
import com.yotfr.weather.domain.weather.model.WeatherData

data class WeatherInfoState(
    val isLoading: Boolean = false,
    val currentTime: String = "",
    @DrawableRes val currentWeatherTypeIconRes: Int ? = null,
    val currentWeatherTypeDescription: String = "",
    val currentTemperature: String = "",
    val currentPressure: String = "",
    val currentHumidity: String = "",
    val currentWindSpeed: String = "",
    val hourlyWeatherListForToday: List<WeatherData> ? = emptyList(),
    val hourlyWeatherListForTomorrow: List<WeatherData> ? = emptyList(),
    val dayAfterTomorrowDate: String ? = "",
    val hourlyWeatherListForDayAfterTomorrow: List<WeatherData> ? = emptyList(),
    val inTwoDaysDate: String ? = "",
    val hourlyWeatherListForInTwoDays: List<WeatherData> ? = emptyList(),
    val inThreeDaysDate: String ? = "",
    val hourlyWeatherListForInThreeDays: List<WeatherData> ? = emptyList(),
    val inFourDaysDate: String ? = "",
    val hourlyWeatherListForInFourDays: List<WeatherData> ? = emptyList(),
    val inFiveDaysDate: String ? = "",
    val hourlyWeatherListForInFiveDays: List<WeatherData> ? = emptyList()
)
