package com.yotfr.weather.data.datasource.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily")
data class DailyWeatherCacheEntity(
    @PrimaryKey val id: Long,
    val time: List<String>,
    val weatherCodes: List<Int>,
    val maxTemperature: List<Double>,
    val minTemperature: List<Double>,
    val sunrise: List<String>,
    val sunset: List<String>
)