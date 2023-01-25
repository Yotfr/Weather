package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.PlaceInfo
import com.yotfr.weather.domain.repository.PlacesRepository
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.Flow

/**
 * [SearchPlacesUseCase] returns places whose names matching searchQuery
 */
class SearchPlacesUseCase(
    private val placesRepository: PlacesRepository
) {
    suspend operator fun invoke(searchQuery: String): Flow<Response<List<PlaceInfo>>> {
        return placesRepository.getPlacesThatMatchesQuery(
            searchQuery = searchQuery
        )
    }
}
