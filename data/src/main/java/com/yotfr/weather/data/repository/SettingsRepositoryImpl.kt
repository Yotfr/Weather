package com.yotfr.weather.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.yotfr.weather.domain.model.MeasuringUnits
import com.yotfr.weather.domain.model.TemperatureUnits
import com.yotfr.weather.domain.model.WindSpeedUnits
import com.yotfr.weather.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    override suspend fun updateTemperatureUnit(temperatureUnits: TemperatureUnits) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TEMPERATURE_UNIT] = temperatureUnits.stringName
        }
    }

    override suspend fun updateWindSpeedUnit(windSpeedUnits: WindSpeedUnits) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.WIND_SPEED_UNIT] = windSpeedUnits.stringName
        }
    }

    override fun getMeasuringUnitsFlow(): Flow<MeasuringUnits> {
        val measuringUnits = dataStore.data
            .map { preferences ->

                val temperatureString = preferences[PreferencesKeys.TEMPERATURE_UNIT]
                val temperature = TemperatureUnits.values()
                    .firstOrNull { it.stringName == temperatureString } ?: TemperatureUnits.CELSIUS

                val windSpeedString = preferences[PreferencesKeys.WIND_SPEED_UNIT]
                val windSpeed = WindSpeedUnits.values()
                    .firstOrNull { it.stringName == windSpeedString } ?: WindSpeedUnits.KMH

                MeasuringUnits(
                    temperatureUnit = temperature,
                    windSpeedUnit = windSpeed
                )
            }
        return measuringUnits
    }

    override suspend fun getMeasuringUnitsValues(): MeasuringUnits {
        val measuringUnits = dataStore.data
            .map { preferences ->

                val temperatureString = preferences[PreferencesKeys.TEMPERATURE_UNIT]
                val temperature = TemperatureUnits.values()
                    .firstOrNull { it.stringName == temperatureString } ?: TemperatureUnits.CELSIUS

                val windSpeedString = preferences[PreferencesKeys.WIND_SPEED_UNIT]
                val windSpeed = WindSpeedUnits.values()
                    .firstOrNull { it.stringName == windSpeedString } ?: WindSpeedUnits.KMH

                MeasuringUnits(
                    temperatureUnit = temperature,
                    windSpeedUnit = windSpeed
                )
            }.first()
        return measuringUnits
    }

    private object PreferencesKeys {
        val TEMPERATURE_UNIT = stringPreferencesKey("TEMPERATURE_UNIT")
        val WIND_SPEED_UNIT = stringPreferencesKey("WIND_SPEED_UNIT")
    }
}
