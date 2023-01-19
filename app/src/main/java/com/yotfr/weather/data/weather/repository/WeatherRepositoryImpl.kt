package com.yotfr.weather.data.weather.repository

import com.yotfr.weather.data.places.datasource.local.WeatherDao
import com.yotfr.weather.data.weather.datasource.remote.WeatherApi
import com.yotfr.weather.data.util.mapToWeatherDataEntity
import com.yotfr.weather.data.util.mapToWeatherInfo
import com.yotfr.weather.domain.weather.model.WeatherInfo
import com.yotfr.weather.domain.weather.repository.WeatherRepository
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
    private val weatherDao: WeatherDao
) : WeatherRepository {

    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Flow<Response<WeatherInfo>> = flow {
        emit(Response.Loading<WeatherInfo>())

        val cachedWeatherData = weatherDao.getWeatherData()
        cachedWeatherData?.let {
            emit(Response.Loading(it.mapToWeatherInfo()))
        }

        try {
            val fetchedWeatherData = weatherApi.getWeatherData(
                latitude = latitude,
                longitude = longitude,
                timezone = TimeZone.getDefault().id
            )
            weatherDao.deleteWeatherData()
            weatherDao.insertWeatherData(fetchedWeatherData.mapToWeatherDataEntity())
        } catch (e: Exception) {
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

        val newCachedWeatherData = weatherDao.getWeatherData()
        newCachedWeatherData?.let {
            emit(
                Response.Success(
                    data = it.mapToWeatherInfo()
                )
            )
        }
    }
}
