package com.yotfr.weather.domain.usecase

import android.util.Log
import com.yotfr.weather.domain.location.LocationTracker
import com.yotfr.weather.domain.model.weather.WeatherInfo
import com.yotfr.weather.domain.repository.WeatherRepository
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoadWeatherInfoUseCase(
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker
) {
    suspend operator fun invoke(): Flow<Response<WeatherInfo>> {
        return locationTracker.getCurrentLocation()?.let { location ->
            Log.d("TEST","repo with ${location.latitude} ${location.longitude}")
            weatherRepository.getWeatherData(
                latitude = location.latitude,
                longitude = location.longitude
            )
        } ?: kotlin.run {
            flow { Response.Error<WeatherInfo>(message = "couldn't retrieve location") }
        }
    }
}
