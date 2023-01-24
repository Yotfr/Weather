package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.location.LocationTracker
import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.repository.PlacesRepository
import com.yotfr.weather.domain.repository.SettingsRepository
import com.yotfr.weather.domain.repository.WeatherRepository
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.*
import java.util.TimeZone

class GetWeatherInfoForFavoritePlace(
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker,
    private val placesRepository: PlacesRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(favoritePlaceId: Long?): Flow<Response<FavoritePlaceInfo>> = flow {
        // emit loading state
        emit(Response.Loading())

        if (favoritePlaceId != null && favoritePlaceId != -2L) {
            // In case place is not current place by geolocation

            val getFavoritePlaceResponse = placesRepository.getFavoritePlace(
                placeId = favoritePlaceId,
                isCacheUpdated = false
            )
            emit(getFavoritePlaceResponse)

            getFavoritePlaceResponse.data?.let { favoritePlaceInfo ->
                val temperatureUnits = settingsRepository.getTemperatureUnits(overload = true)
                val windSpeedUnits = settingsRepository.getWindSpeedUnits(overload = true)

                val updateWeatherCacheResponse = weatherRepository.updateWeatherCache(
                    placeId = favoritePlaceInfo.id,
                    latitude = favoritePlaceInfo.latitude,
                    longitude = favoritePlaceInfo.longitude,
                    timeZone = favoritePlaceInfo.timeZone,
                    temperatureUnits = temperatureUnits.stringName,
                    windSpeedUnits = windSpeedUnits.stringName
                )

                updateWeatherCacheResponse?.let {
                    emit(it)
                }

                val getFavoritePlaceResponseCacheUpdated = placesRepository.getFavoritePlace(
                    placeId = favoritePlaceId,
                    isCacheUpdated = true
                )
                emit(getFavoritePlaceResponseCacheUpdated)
            }
        } else {
            // In case place is current place by geolocation
            val getFavoritePlaceResponse = placesRepository.getFavoritePlace(
                placeId = -2L,
                isCacheUpdated = false
            )
            emit(getFavoritePlaceResponse)

            val locationResponse = locationTracker.getCurrentLocation()

            locationResponse?.data?.let { location ->
                val updateCurrentPlaceResponse = placesRepository.updateCurrentPlaceInfo(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
                updateCurrentPlaceResponse?.let {
                    emit(updateCurrentPlaceResponse)
                }

                val temperatureUnits = settingsRepository.getTemperatureUnits(overload = true)
                val windSpeedUnits = settingsRepository.getWindSpeedUnits(overload = true)

                val updateWeatherCacheResponse = weatherRepository.updateWeatherCache(
                    placeId = -2L,
                    latitude = location.latitude,
                    longitude = location.longitude,
                    timeZone = TimeZone.getDefault().id,
                    temperatureUnits = temperatureUnits.stringName,
                    windSpeedUnits = windSpeedUnits.stringName
                )
                updateWeatherCacheResponse?.let {
                    emit(it)
                }

                val getFavoritePlaceResponseCacheUpdated = placesRepository.getFavoritePlace(
                    placeId = -2L,
                    isCacheUpdated = true
                )
                emit(getFavoritePlaceResponseCacheUpdated)
            }
        }
    }
}
