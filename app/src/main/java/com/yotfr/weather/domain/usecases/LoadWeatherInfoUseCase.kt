package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.location.LocationTracker
import com.yotfr.weather.domain.model.WeatherInfo
import com.yotfr.weather.domain.repository.WeatherRepository
import com.yotfr.weather.domain.util.Cause
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.TimeZone

class LoadWeatherInfoUseCase(
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker
) {
    suspend operator fun invoke(
        latitude: Double ? = null,
        longitude: Double ? = null,
        timezone: String ? = null
    ): Flow<Response<WeatherInfo>> {
        return if (latitude != null && longitude != null && timezone != null) {
            weatherRepository.getWeatherData(
                latitude = latitude,
                longitude = longitude,
                timeZone = timezone
            )
        }else {
            locationTracker.getCurrentLocation()?.let { location ->
                weatherRepository.getWeatherData(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    timeZone = TimeZone.getDefault().id
                )
            } ?: kotlin.run {
                flow { Response.Exception(cause = Cause.UnknownException()) }
            }
        }
    }
}
