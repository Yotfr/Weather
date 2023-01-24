package com.yotfr.weather.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yotfr.weather.data.datasource.local.entities.FavoritePlaceEntity
import com.yotfr.weather.data.datasource.local.entities.WeatherCacheEntity
import com.yotfr.weather.data.util.ListConverters

@Database(
    entities = [
        WeatherCacheEntity::class,
        FavoritePlaceEntity::class
    ],
    version = 1
)
@TypeConverters(ListConverters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract val weatherCacheDao: WeatherCacheDao
    abstract val placesDao: PlacesDao
}
