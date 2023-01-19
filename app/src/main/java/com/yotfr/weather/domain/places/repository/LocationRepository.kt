package com.yotfr.weather.domain.places.repository

import com.yotfr.weather.domain.places.model.LocationInfo
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getLocation(searchQuery: String): Flow<Response<List<LocationInfo>>>
}
