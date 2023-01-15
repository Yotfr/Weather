package com.yotfr.weather.data.repository

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
    private val weatherApi: WeatherApi
) : WeatherRepository {

    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Flow<Response<WeatherInfo>> = flow {
        try {
            emit(Response.Loading)
            emit(
                Response.Success(
                    data = weatherApi.getWeatherData(
                        latitude = latitude,
                        longitude = longitude,
                        timezone = TimeZone.getDefault().id
                    ).mapToWeatherInfo()
                )
            )
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
    }
}
