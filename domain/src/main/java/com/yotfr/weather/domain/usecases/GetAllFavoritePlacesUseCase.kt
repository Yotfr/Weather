package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow

/**
 * [GetAllFavoritePlacesUseCase] returns information about all favorite places
 */
class GetAllFavoritePlacesUseCase(
    private val placesRepository: PlacesRepository
) {
    suspend operator fun invoke(): Flow<List<FavoritePlaceInfo>> {
        return placesRepository.getFavoritePlaces()
    }
}
