package com.yotfr.weather.data.datasource.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weathercache")
data class WeatherCacheEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val dailyTime: List<String>,
    val dailyWeatherCodes: List<Int>,
    val dailyMaxTemperature: List<Double>,
    val dailyMinTemperature: List<Double>,
    val dailySunrise: List<String>,
    val dailySunset: List<String>,
    val hourlyTime: List<String>,
    val hourlyTemperatures: List<Double>,
    val hourlyWeatherCodes: List<Int>,
    val hourlyPressures: List<Double>,
    val hourlyWindSpeeds: List<Double>,
    val hourlyHumidities: List<Double>,
    val hourlyApparentTemperature: List<Double>,
    val placeId: Long
)
