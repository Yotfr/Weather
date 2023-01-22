package com.yotfr.weather.data.util

import com.yotfr.weather.data.datasource.local.entities.FavoritePlaceEntity
import com.yotfr.weather.data.datasource.local.relation.PlaceWithWeatherCache
import com.yotfr.weather.data.datasource.remote.dto.PlaceDataDto
import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.model.PlaceInfo

fun PlaceWithWeatherCache.mapToFavoritePlaceInfo(): FavoritePlaceInfo {
    return FavoritePlaceInfo(
        id = favoritePlaceEntity.id,
        placeName = favoritePlaceEntity.placeName,
        latitude = favoritePlaceEntity.latitude,
        longitude = favoritePlaceEntity.longitude,
        countryName = favoritePlaceEntity.countryName,
        timeZone = favoritePlaceEntity.timeZone,
        weatherInfo = weatherCacheEntity?.mapToWeatherInfo()
    )
}

fun PlaceInfo.mapToFavoritePlaceEntity(): FavoritePlaceEntity {
    return FavoritePlaceEntity(
        id = id,
        placeName = placeName,
        latitude = latitude,
        longitude = longitude,
        countryName = countryName,
        timeZone = timeZone
    )
}

fun FavoritePlaceInfo.mapToFavoritePlaceEntity(): FavoritePlaceEntity {
    return FavoritePlaceEntity(
        id = id,
        placeName = placeName,
        latitude = latitude,
        longitude = longitude,
        countryName = countryName,
        timeZone = timeZone
    )
}

fun PlaceDataDto.mapToPlaceInfo(): PlaceInfo {
    return PlaceInfo(
        id = id,
        placeName = placeName,
        latitude = latitude,
        longitude = longitude,
        countryName = countryName,
        timeZone = timeZone
    )
}
