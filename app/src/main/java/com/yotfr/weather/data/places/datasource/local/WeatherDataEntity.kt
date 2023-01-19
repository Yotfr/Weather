package com.yotfr.weather.data.places.datasource.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weatherdata")
data class WeatherDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val time: List<String>,
    val temperatures: List<Double>,
    val weatherCodes: List<Int>,
    val pressures: List<Double>,
    val windSpeeds: List<Double>,
    val humidities: List<Double>
)
