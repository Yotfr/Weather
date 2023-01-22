package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.PlaceInfo
import com.yotfr.weather.domain.repository.PlacesRepository
import com.yotfr.weather.domain.repository.WeatherRepository

class AddPlaceToFavoriteUseCase(
    private val placesRepository: PlacesRepository,
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(place: PlaceInfo) {
        // Add searched place to favorite place table
        val placeId = placesRepository.addFavoritePlace(
            place = place
        )
        // Fetch weather data for this place and cache it
        weatherRepository.fetchAndCacheWeatherDataForPlaceId(
            placeId = placeId,
            latitude = place.latitude,
            longitude = place.longitude,
            timeZone = place.timeZone
        )
    }
}