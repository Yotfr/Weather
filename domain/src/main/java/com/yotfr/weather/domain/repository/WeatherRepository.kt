package com.yotfr.weather.domain.repository

import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.model.PlaceInfo
import com.yotfr.weather.domain.model.WeatherInfo
import com.yotfr.weather.domain.util.Response

interface WeatherRepository {

    suspend fun fetchAndCacheWeatherDataForPlaceId(
        placeId: Long,
        latitude: Double,
        longitude: Double,
        timeZone: String,
        temperatureUnits: String,
        windSpeedUnits: String
    )

    suspend fun updateWeatherCache(
        placeId: Long,
        latitude: Double,
        longitude: Double,
        timeZone: String,
        temperatureUnits: String,
        windSpeedUnits: String
    ): Response<FavoritePlaceInfo>?

    suspend fun deleteWeatherDataForFavoritePlace(
        placeId: Long
    )

    suspend fun getWeatherDataForSearchedPlace(
        placeInfo: PlaceInfo,
        temperatureUnits: String,
        windSpeedUnits: String
    ): Response<FavoritePlaceInfo>
}
