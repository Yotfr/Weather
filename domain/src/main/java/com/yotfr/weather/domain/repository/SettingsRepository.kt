package com.yotfr.weather.domain.repository

import com.yotfr.weather.domain.model.MeasuringUnits
import com.yotfr.weather.domain.model.TemperatureUnits
import com.yotfr.weather.domain.model.WindSpeedUnits
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun updateTemperatureUnit(temperatureUnits: TemperatureUnits)
    suspend fun updateWindSpeedUnit(windSpeedUnits: WindSpeedUnits)
    fun getMeasuringUnitsFlow(): Flow<MeasuringUnits>
    suspend fun getMeasuringUnitsValues(): MeasuringUnits
}
