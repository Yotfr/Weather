package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.PlaceInfo
import com.yotfr.weather.domain.repository.PlacesRepository
import com.yotfr.weather.domain.repository.SettingsRepository
import com.yotfr.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.last

class AddPlaceToFavoriteUseCase(
    private val placesRepository: PlacesRepository,
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(place: PlaceInfo) {
        // Add searched place to favorite place table
        val placeId = placesRepository.addFavoritePlace(
            place = place
        )
        val temperatureUnits = settingsRepository.getTemperatureUnits(overload = true)
        val windSpeedUnits = settingsRepository.getWindSpeedUnits(overload = true)
        // Fetch weather data for this place and cache it
        weatherRepository.fetchAndCacheWeatherDataForPlaceId(
            placeId = placeId,
            latitude = place.latitude,
            longitude = place.longitude,
            timeZone = place.timeZone,
            temperatureUnits = temperatureUnits.stringName,
            windSpeedUnits = windSpeedUnits.stringName
        )
    }
}