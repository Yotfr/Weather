package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.LocationInfo
import com.yotfr.weather.domain.repository.LocationRepository
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
