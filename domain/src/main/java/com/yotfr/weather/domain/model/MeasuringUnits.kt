package com.yotfr.weather.domain.model

/**
 * [MeasuringUnits] contains information about the measurement units selected by the user
 */
data class MeasuringUnits(
    val temperatureUnit: TemperatureUnits,
    val windSpeedUnit: WindSpeedUnits
)
