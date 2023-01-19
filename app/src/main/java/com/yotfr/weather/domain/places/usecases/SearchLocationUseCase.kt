package com.yotfr.weather.domain.places.usecases

import com.yotfr.weather.domain.places.model.LocationInfo
import com.yotfr.weather.domain.places.repository.LocationRepository
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.Flow

class SearchLocationUseCase(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(searchQuery: String): Flow<Response<List<LocationInfo>>> {
        return locationRepository.getLocation(
            searchQuery = searchQuery
        )
    }
}
