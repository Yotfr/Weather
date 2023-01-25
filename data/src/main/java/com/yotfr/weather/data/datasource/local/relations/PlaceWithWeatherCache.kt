package com.yotfr.weather.data.datasource.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.yotfr.weather.data.datasource.local.entities.FavoritePlaceEntity
import com.yotfr.weather.data.datasource.local.entities.WeatherCacheEntity

data class PlaceWithWeatherCache(
    @Embedded val favoritePlaceEntity: FavoritePlaceEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "placeId"
    )
    val weatherCacheEntity: WeatherCacheEntity?
)
