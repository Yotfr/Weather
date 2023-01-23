package com.yotfr.weather.presentation.settings

import com.yotfr.weather.domain.model.TemperatureUnits
import com.yotfr.weather.domain.model.WindSpeedUnits

data class SettingsState(
    val temperatureUnits: TemperatureUnits = TemperatureUnits.CELSIUS,
    val windSpeedUnits: WindSpeedUnits = WindSpeedUnits.KMH
)
