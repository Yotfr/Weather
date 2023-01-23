package com.yotfr.weather.domain.repository

import com.yotfr.weather.domain.model.TemperatureUnits
import com.yotfr.weather.domain.model.WindSpeedUnits
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun updateTemperatureUnits(temperatureUnits: TemperatureUnits)
    fun getTemperatureUnits(): Flow<TemperatureUnits>
    suspend fun updateWindSpeedUnits(windSpeedUnits: WindSpeedUnits)
    fun getWindSpeedUnits(): Flow<WindSpeedUnits>
    suspend fun getTemperatureUnits(overload: Boolean = true): TemperatureUnits
    suspend fun getWindSpeedUnits(overload: Boolean = true): WindSpeedUnits
}
