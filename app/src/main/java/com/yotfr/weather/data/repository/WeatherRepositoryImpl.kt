package com.yotfr.weather.data.repository

import android.util.Log
import com.yotfr.weather.data.datasource.local.WeatherCacheDao
import com.yotfr.weather.data.datasource.remote.WeatherApi
import com.yotfr.weather.data.util.mapToWeatherCacheEntity
import com.yotfr.weather.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val weatherCacheDao: WeatherCacheDao
) : WeatherRepository {

    override suspend fun fetchAndCacheWeatherDataForPlaceId(
        placeId: Long,
        latitude: Double,
        longitude: Double,
        timeZone: String
    ) {
        val fetchedData = weatherApi.getWeatherData(
            latitude = latitude,
            longitude = longitude,
            timezone = timeZone
        )
        weatherCacheDao.insertWeatherCache(
            weatherData = fetchedData.mapToWeatherCacheEntity(
                placeId = placeId
            )
        )
    }

    override suspend fun updateWeatherCacheForFavoritePlace(
        placeId: Long,
        latitude: Double,
        longitude: Double,
        timeZone: String
    ) {
        val fetchedData = weatherApi.getWeatherData(
            latitude = latitude,
            longitude = longitude,
            timezone = timeZone
        )
        weatherCacheDao.deleteWeatherCache(
            placeId = placeId
        )
        weatherCacheDao.insertWeatherCache(
            weatherData = fetchedData.mapToWeatherCacheEntity(
                placeId = placeId
            )
        )
    }
}
