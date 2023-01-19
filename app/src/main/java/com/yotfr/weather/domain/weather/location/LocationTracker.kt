package com.yotfr.weather.domain.weather.location

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}
