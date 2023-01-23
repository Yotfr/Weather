package com.yotfr.weather.presentation.settings

import com.yotfr.weather.domain.model.TemperatureUnits
import com.yotfr.weather.domain.model.WindSpeedUnits

sealed interface SettingsEvent {
    data class TemperatureUnitsChanged(val newTemperatureUnit: TemperatureUnits) : SettingsEvent
    data class WindSpeedUnitsChanged(val newWindSpeedUnits: WindSpeedUnits) : SettingsEvent
}
