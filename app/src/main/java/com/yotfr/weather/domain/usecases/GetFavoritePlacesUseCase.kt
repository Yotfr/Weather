package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow

class GetFavoritePlacesUseCase(
    private val placesRepository: PlacesRepository
) {
    suspend operator fun invoke(): Flow<List<FavoritePlaceInfo>> {
        return placesRepository.getFavoritePlaces()
    }
}