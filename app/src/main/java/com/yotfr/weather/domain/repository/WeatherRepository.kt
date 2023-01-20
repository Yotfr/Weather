package com.yotfr.weather.domain.repository

import com.yotfr.weather.domain.model.WeatherInfo
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherData(
        latitude: Double,
        longitude: Double,
        timeZone: String
    ): Flow<Response<WeatherInfo>>
}
