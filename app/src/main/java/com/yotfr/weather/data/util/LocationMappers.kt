package com.yotfr.weather.data.util

import com.yotfr.weather.data.datasource.local.entities.PlaceEntity
import com.yotfr.weather.data.datasource.remote.dto.PlaceDataDto
import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.model.PlaceInfo
import com.yotfr.weather.domain.model.WeatherType

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

fun PlaceEntity.mapToFavoritePlaceInfo(): FavoritePlaceInfo {
    return FavoritePlaceInfo(
        id = id,
        placeName = placeName,
        latitude = latitude,
        longitude = longitude,
        countryName = countryName,
        timeZone = timeZone,
        weatherCode = WeatherType.fromWMO(
            code = weatherCode,
            isDayTime = true
        ),
        temperature = temperature
    )
}

fun PlaceInfo.mapToLocationInfo(
    weatherCode: Int,
    temperature: Double
): PlaceEntity {
    return PlaceEntity(
        id = id,
        placeName = placeName,
        latitude = latitude,
        longitude = longitude,
        countryName = countryName,
        timeZone = timeZone,
        weatherCode = weatherCode,
        temperature = temperature
    )
}
