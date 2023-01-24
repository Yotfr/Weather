package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.repository.PlacesRepository
import com.yotfr.weather.domain.repository.WeatherRepository

class DeleteFavoritePlaceUseCase(
    private val placesRepository: PlacesRepository,
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(place: FavoritePlaceInfo) {
        placesRepository.deleteFavoritePlace(place)
        weatherRepository.deleteWeatherDataForFavoritePlace(place.id)
    }
}