package com.yotfr.weather.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [WeatherDataEntity::class],
    version = 1
)
@TypeConverters(ListConverters::class)
abstract class WeatherDataBase : RoomDatabase() {
    abstract val weatherDao: WeatherDao
}
