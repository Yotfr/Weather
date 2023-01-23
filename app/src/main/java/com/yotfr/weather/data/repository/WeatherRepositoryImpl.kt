package com.yotfr.weather.data.repository

import com.yotfr.weather.data.datasource.local.WeatherCacheDao
import com.yotfr.weather.data.datasource.remote.WeatherApi
import com.yotfr.weather.data.util.mapToWeatherCacheEntity
import com.yotfr.weather.data.util.mapToWeatherInfo
import com.yotfr.weather.domain.model.WeatherInfo
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
        timeZone: String,
        temperatureUnits: String,
        windSpeedUnits: String
    ) {
        val fetchedData = weatherApi.getWeatherData(
            latitude = latitude,
            longitude = longitude,
            timezone = timeZone,
            temperatureUnits = temperatureUnits,
            windSpeedUnits = windSpeedUnits
        )
        weatherCacheDao.insertWeatherCache(
            weatherData = fetchedData.mapToWeatherCacheEntity(
                placeId = placeId
            )
        )
    }

    override suspend fun deleteWeatherDataForFavoritePlace(placeId: Long) {
        weatherCacheDao.deleteWeatherCache(placeId)
    }

    override suspend fun getWeatherDataForSearchedPlace(
        latitude: Double,
        longitude: Double,
        timeZone: String,
        temperatureUnits: String,
        windSpeedUnits: String
    ): WeatherInfo {
        return weatherApi.getWeatherData(
            latitude = latitude,
            longitude = longitude,
            timezone = timeZone,
            temperatureUnits = temperatureUnits,
            windSpeedUnits = windSpeedUnits
        ).mapToWeatherInfo(
            timeZone = timeZone
        )
    }

    override suspend fun updateWeatherCacheForFavoritePlace(
        placeId: Long,
        latitude: Double,
        longitude: Double,
        timeZone: String,
        temperatureUnits: String,
        windSpeedUnits: String
    ) {
        val fetchedData = weatherApi.getWeatherData(
            latitude = latitude,
            longitude = longitude,
            timezone = timeZone,
            temperatureUnits = temperatureUnits,
            windSpeedUnits = windSpeedUnits
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
