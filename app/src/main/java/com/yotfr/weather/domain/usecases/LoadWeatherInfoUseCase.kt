package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.location.LocationTracker
import com.yotfr.weather.domain.model.WeatherInfo
import com.yotfr.weather.domain.repository.WeatherRepository
import com.yotfr.weather.domain.util.Cause
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoadWeatherInfoUseCase(
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker
) {
    suspend operator fun invoke(): Flow<Response<WeatherInfo>> {
        return locationTracker.getCurrentLocation()?.let { location ->
            weatherRepository.getWeatherData(
                latitude = location.latitude,
                longitude = location.longitude
            )
        } ?: kotlin.run {
            flow { Response.Exception(cause = Cause.UnknownException()) }
        }
    }
}
