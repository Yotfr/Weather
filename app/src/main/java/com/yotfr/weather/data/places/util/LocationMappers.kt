package com.yotfr.weather.data.places.util

import com.yotfr.weather.data.places.datasource.remote.dto.LocationDataDto
import com.yotfr.weather.domain.places.model.LocationInfo

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