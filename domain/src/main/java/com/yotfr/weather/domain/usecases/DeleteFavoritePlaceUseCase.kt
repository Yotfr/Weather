package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.repository.PlacesRepository
import com.yotfr.weather.domain.repository.WeatherRepository

/**
 * [DeleteFavoritePlaceUseCase] delete information about place from the database and also
 * deletes information about this place
 */
class DeleteFavoritePlaceUseCase(
    private val placesRepository: PlacesRepository,
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(place: FavoritePlaceInfo) {
        placesRepository.deleteFavoritePlace(place)
        weatherRepository.deleteWeatherDataRelatedToPlaceId(place.id)
    }
}