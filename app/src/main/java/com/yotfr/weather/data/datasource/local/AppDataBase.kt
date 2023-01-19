package com.yotfr.weather.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yotfr.weather.data.datasource.local.entities.DailyWeatherCacheEntity
import com.yotfr.weather.data.datasource.local.entities.PlaceEntity
import com.yotfr.weather.data.datasource.local.entities.HourlyWeatherCacheEntity
import com.yotfr.weather.data.util.ListConverters

@Database(
    entities = [
        HourlyWeatherCacheEntity::class,
        DailyWeatherCacheEntity::class,
        PlaceEntity::class
    ],
    version = 1
)
@TypeConverters(ListConverters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract val weatherCacheDao: WeatherCacheDao
    abstract val placesDao: PlacesDao
}
