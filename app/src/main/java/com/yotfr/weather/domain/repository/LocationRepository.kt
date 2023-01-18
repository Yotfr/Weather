package com.yotfr.weather.domain.repository

import com.yotfr.weather.domain.model.LocationInfo
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getLocation(searchQuery: String): Flow<Response<List<LocationInfo>>>
}
