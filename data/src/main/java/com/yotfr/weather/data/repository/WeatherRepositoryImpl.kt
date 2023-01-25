package com.yotfr.weather.data.repository

import com.yotfr.weather.data.datasource.local.WeatherCacheDao
import com.yotfr.weather.data.datasource.remote.WeatherApi
import com.yotfr.weather.data.util.mapToWeatherCacheEntity
import com.yotfr.weather.data.util.mapToWeatherInfo
import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.model.PlaceInfo
import com.yotfr.weather.domain.repository.WeatherRepository
import com.yotfr.weather.domain.util.Cause
import com.yotfr.weather.domain.util.Response
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val weatherCacheDao: WeatherCacheDao
) : WeatherRepository {

    /*
     Fetch information about the weather in place with selected place id
     and save it to the database
     */
    override suspend fun createWeatherCacheForPlace(
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

    // Delete weather cache related to selected placeId
    override suspend fun deleteWeatherDataRelatedToPlaceId(placeId: Long) {
        weatherCacheDao.deleteWeatherCache(placeId)
    }

    // Fetch and return information about the weather in selected place
    override suspend fun getWeatherDataForSearchedPlace(
        placeInfo: PlaceInfo,
        temperatureUnits: String,
        windSpeedUnits: String
    ): Response<FavoritePlaceInfo> {
        try {
            val fetchedWeatherData = weatherApi.getWeatherData(
                latitude = placeInfo.latitude,
                longitude = placeInfo.longitude,
                timezone = placeInfo.timeZone,
                temperatureUnits = temperatureUnits,
                windSpeedUnits = windSpeedUnits
            )
            val mappedFetchedWeatherData = fetchedWeatherData.mapToWeatherInfo(
                timeZone = placeInfo.timeZone
            )
            return Response.Success(
                data = FavoritePlaceInfo(
                    id = placeInfo.id,
                    placeName = placeInfo.placeName,
                    latitude = placeInfo.latitude,
                    longitude = placeInfo.longitude,
                    countryName = placeInfo.countryName,
                    timeZone = placeInfo.timeZone,
                    weatherInfo = mappedFetchedWeatherData
                )
            )
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    return Response.Exception(
                        cause = Cause.UnknownException()
                    )
                }
                is IOException -> {
                    return Response.Exception(
                        cause = Cause.BadConnectionException
                    )
                }
                else -> {
                    return Response.Exception(
                        cause = Cause.UnknownException()
                    )
                }
            }
        }
    }

    /*
     Fetch information about the weather in place with selected place id
     and update database data
     */
    override suspend fun updateWeatherCacheRelatedToPlaceId(
        placeId: Long,
        latitude: Double,
        longitude: Double,
        timeZone: String,
        temperatureUnits: String,
        windSpeedUnits: String
    ): Response<FavoritePlaceInfo>? {
        try {
            val fetchedWeather = weatherApi.getWeatherData(
                latitude = latitude,
                longitude = longitude,
                timezone = timeZone,
                temperatureUnits = temperatureUnits,
                windSpeedUnits = windSpeedUnits
            )
            val mappedFetchedWeather = fetchedWeather.mapToWeatherCacheEntity(
                placeId = placeId
            )
            weatherCacheDao.deleteWeatherCache(
                placeId = placeId
            )
            weatherCacheDao.insertWeatherCache(
                weatherData = mappedFetchedWeather
            )
            return null
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    return Response.Exception(
                        cause = Cause.UnknownException()
                    )
                }
                is IOException -> {
                    return Response.Exception(
                        cause = Cause.BadConnectionException
                    )
                }
                else -> {
                    return Response.Exception(
                        cause = Cause.UnknownException()
                    )
                }
            }
        }
    }
}
