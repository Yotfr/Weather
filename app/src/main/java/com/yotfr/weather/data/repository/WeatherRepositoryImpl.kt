package com.yotfr.weather.data.repository

import android.util.Log
import com.yotfr.weather.data.mapper.mapToWeatherInfo
import com.yotfr.weather.data.remote.WeatherApi
import com.yotfr.weather.domain.model.weather.WeatherInfo
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
                Response.Succecss(
                    data = weatherApi.getWeatherData(
                        latitude = latitude,
                        longitude = longitude
                    ).mapToWeatherInfo()
                )
            )
        } catch (e: Exception) {
            Log.d("TEST","exception $e ${e.message} ${e.cause}")
            e.printStackTrace()
            emit(
                Response.Error(
                    message = e.message ?: "Unknown error"
                )
            )
        }
    }
}
