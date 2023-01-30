package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.repository.PlacesRepository

class CheckIfPlaceExistsInDatabaseUseCase(
    private val placesRepository: PlacesRepository
) {
    suspend operator fun invoke(placeId: Long): Boolean {
        return placesRepository.checkIfFavoritePlaceExistsInDatabase(
            placeId = placeId
        )
    }
}
