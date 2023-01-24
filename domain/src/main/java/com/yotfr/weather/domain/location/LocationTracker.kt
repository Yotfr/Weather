package com.yotfr.weather.domain.location

import com.yotfr.weather.domain.model.Location
import com.yotfr.weather.domain.util.Response

interface LocationTracker {
    suspend fun getCurrentLocation(): Response<Location>?
}
