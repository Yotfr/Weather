package com.yotfr.weather.domain.repository

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
}
