package com.yotfr.weather.data.util

import com.yotfr.weather.data.datasource.remote.dto.LocationDataDto
import com.yotfr.weather.domain.model.LocationInfo

fun LocationDataDto.mapToLocationInfo(): LocationInfo {
    return LocationInfo(
        id = id,
        placeName = placeName,
        latitude = latitude,
        longitude = longitude,
        countryName = countryName,
        timeZone = timeZone
    )
}