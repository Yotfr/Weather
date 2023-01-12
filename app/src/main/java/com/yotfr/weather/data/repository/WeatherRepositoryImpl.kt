package com.yotfr.weather.data.repository

import com.yotfr.weather.data.datasource.remote.WeatherApi
import com.yotfr.weather.data.util.mapToWeatherInfo
import com.yotfr.weather.domain.model.WeatherInfo
import com.yotfr.weather.domain.repository.WeatherRepository
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherRepository {

    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): Flow<Response<WeatherInfo>> = flow {
        try {
            emit(
                Response.Success(
                    data = weatherApi.getWeatherData(
                        latitude = latitude,
                        longitude = longitude
                    ).mapToWeatherInfo()
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                Response.Error(
                    message = e.message ?: "Unknown error"
                )
            )
        }
    }
}
