package com.yotfr.weather.data.util

import com.yotfr.weather.data.datasource.local.entities.PlaceEntity
import com.yotfr.weather.data.datasource.remote.dto.PlaceDataDto
import com.yotfr.weather.domain.model.PlaceInfo

fun PlaceDataDto.mapToLocationInfo(): PlaceInfo {
    return PlaceInfo(
        id = id,
        placeName = placeName,
        latitude = latitude,
        longitude = longitude,
        countryName = countryName,
        timeZone = timeZone
    )
}

fun PlaceEntity.mapToLocationInfo(): PlaceInfo {
    return PlaceInfo(
        id = id,
        placeName = placeName,
        latitude = latitude,
        longitude = longitude,
        countryName = countryName,
        timeZone = timeZone
    )
}

fun PlaceInfo.mapToLocationInfo(): PlaceEntity {
    return PlaceEntity(
        id = id,
        placeName = placeName,
        latitude = latitude,
        longitude = longitude,
        countryName = countryName,
        timeZone = timeZone
    )
}