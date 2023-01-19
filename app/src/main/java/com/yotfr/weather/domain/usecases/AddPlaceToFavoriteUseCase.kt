package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.PlaceInfo
import com.yotfr.weather.domain.repository.PlacesRepository

class AddPlaceToFavoriteUseCase(
    private val placesRepository: PlacesRepository
) {
    suspend operator fun invoke(place: PlaceInfo) {
        return placesRepository.addFavoritePlace(
            place = place
        )
    }
}