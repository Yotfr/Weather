package com.yotfr.weather.domain.repository

import com.yotfr.weather.domain.model.WeatherInfo

interface WeatherRepository {

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

    suspend fun deleteWeatherDataForFavoritePlace(
        placeId: Long
    )

    suspend fun getWeatherDataForSearchedPlace(
        latitude: Double,
        longitude: Double,
        timeZone: String
    ): WeatherInfo
}
