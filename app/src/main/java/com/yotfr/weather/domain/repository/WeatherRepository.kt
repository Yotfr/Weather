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

    suspend fun fetchAndCacheWeatherDataForPlaceId(
        placeId: Long,
        latitude: Double,
        longitude: Double,
        timeZone: String
    )

    suspend fun updateWeatherCacheForFavoritePlace(
        placeId: Long,
        latitude: Double,
        longitude: Double,
        timeZone: String
    )
}
