package com.yotfr.weather.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
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

    override suspend fun updateTemperatureUnits(temperatureUnits: TemperatureUnits) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TEMPERATURE_UNIT] = temperatureUnits.stringName
        }
    }

    override fun getTemperatureUnits(): Flow<TemperatureUnits> {
        val temperatureUnit = dataStore.data
            .map { preferences ->
                val temperature = preferences[PreferencesKeys.TEMPERATURE_UNIT]
                TemperatureUnits.values().firstOrNull { it.stringName == temperature }
                    ?: TemperatureUnits.CELSIUS
            }
        return temperatureUnit
    }

    override suspend fun updateWindSpeedUnits(windSpeedUnits: WindSpeedUnits) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.WIND_SPEED_UNIT] = windSpeedUnits.stringName
        }
    }

    override fun getWindSpeedUnits(): Flow<WindSpeedUnits> {
        val windSpeedUnits = dataStore.data
            .map { preferences ->
                val windSpeed = preferences[PreferencesKeys.WIND_SPEED_UNIT]
                WindSpeedUnits.values().firstOrNull { it.stringName == windSpeed }
                    ?: WindSpeedUnits.KMH
            }
        return windSpeedUnits
    }

    override suspend fun getTemperatureUnits(asFlow: Boolean): TemperatureUnits {
        return dataStore.data.map { preferences ->
            val temperature = preferences[PreferencesKeys.TEMPERATURE_UNIT]
            TemperatureUnits.values().firstOrNull { it.stringName == temperature }
                ?: TemperatureUnits.CELSIUS
        }.first()
    }

    override suspend fun getWindSpeedUnits(overload: Boolean): WindSpeedUnits {
        return dataStore.data.map { preferences ->
            val windSpeed = preferences[PreferencesKeys.WIND_SPEED_UNIT]
            WindSpeedUnits.values().firstOrNull { it.stringName == windSpeed }
                ?: WindSpeedUnits.KMH
        }.first()
    }

    private object PreferencesKeys {
        val TEMPERATURE_UNIT = stringPreferencesKey("TEMPERATURE_UNIT")
        val WIND_SPEED_UNIT = stringPreferencesKey("WIND_SPEED_UNIT")
    }

}