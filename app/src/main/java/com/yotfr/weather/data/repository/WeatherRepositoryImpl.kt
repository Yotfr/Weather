package com.yotfr.weather.data.repository

import android.util.Log
import com.yotfr.weather.data.datasource.local.WeatherCacheDao
import com.yotfr.weather.data.datasource.remote.WeatherApi
import com.yotfr.weather.data.util.mapToWeatherInfo
import com.yotfr.weather.domain.model.WeatherInfo
import com.yotfr.weather.domain.repository.WeatherRepository
import com.yotfr.weather.domain.util.Cause
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.util.TimeZone
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val weatherCacheDao: WeatherCacheDao
) : WeatherRepository {

    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Flow<Response<WeatherInfo>> = flow {
        emit(Response.Loading<WeatherInfo>())

        /*
        Log.d("TEST", "weg")

        val hourlyWeatherCache = weatherCacheDao.getHourlyWeatherCache()
        Log.d("TEST", "$hourlyWeatherCache ")
        val dailyWeatherCache = weatherCacheDao.getDailyWeatherCache()

        Log.d("TEST", "$hourlyWeatherCache g $dailyWeatherCache")

        if (hourlyWeatherCache.isNotEmpty() && dailyWeatherCache.isNotEmpty()) {
            emit(
                Response.Loading(
                    CacheData(
                        hourlyWeatherCacheEntity = hourlyWeatherCache[0],
                        dailyWeatherCacheEntity = dailyWeatherCache[0]
                    ).mapToWeatherInfo()
                )
            )
        }

         */

        try {
            val fetchedWeatherData = weatherApi.getWeatherData(
                latitude = latitude,
                longitude = longitude,
                timezone = TimeZone.getDefault().id
            )
            emit(
                Response.Success(
                    data = fetchedWeatherData.mapToWeatherInfo()
                )
            )
            /*

            Log.d("TEST", "fetched $fetchedWeatherData")
            weatherCacheDao.deleteHourlyWeatherCache()
            weatherCacheDao.insertHourlyWeatherCache(fetchedWeatherData.mapToHourlyCache())
            weatherCacheDao.deleteDailyWeatherCache()
            weatherCacheDao.insertDailyWeatherCache(fetchedWeatherData.mapToDailyCache())

             */
        } catch (e: Exception) {
            Log.d("TEST", "$e")
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        400 -> emit(
                            Response.Exception(
                                cause = Cause.UnknownException(e.message)
                            )
                        )
                        else -> emit(
                            Response.Exception(
                                cause = Cause.UnknownException(e.message)
                            )
                        )
                    }
                }
                is IOException -> {
                    emit(
                        Response.Exception(
                            cause = Cause.BadConnectionException
                        )
                    )
                }
                else -> {
                    emit(
                        Response.Exception(
                            cause = Cause.UnknownException(e.message)
                        )
                    )
                }
            }
        }

        /*
        val hourlyCache = weatherCacheDao.getHourlyWeatherCache()
        val dailyCache = weatherCacheDao.getDailyWeatherCache()
        if (hourlyCache.isNotEmpty() && dailyCache.isNotEmpty()) {
            emit(
                Response.Success(
                    CacheData(
                        hourlyWeatherCacheEntity = hourlyCache[0],
                        dailyWeatherCacheEntity = dailyCache[0]
                    ).mapToWeatherInfo()
                )
            )
        }

         */
    }
}
