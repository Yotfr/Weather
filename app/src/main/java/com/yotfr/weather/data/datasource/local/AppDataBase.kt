package com.yotfr.weather.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yotfr.weather.data.datasource.local.entities.PlaceEntity
import com.yotfr.weather.data.datasource.local.entities.WeatherDataEntity
import com.yotfr.weather.data.util.ListConverters

@Database(
    entities = [
        WeatherDataEntity::class,
        PlaceEntity::class
    ],
    version = 1
)
@TypeConverters(ListConverters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract val weatherDao: WeatherDao
    abstract val placesDao: PlacesDao
}
