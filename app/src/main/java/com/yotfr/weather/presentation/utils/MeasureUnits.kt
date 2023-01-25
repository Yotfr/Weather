package com.yotfr.weather.presentation.utils

import com.yotfr.weather.domain.model.TemperatureUnits
import com.yotfr.weather.domain.model.WindSpeedUnits

fun String.toTemperatureUnitString(
    temperatureUnit: TemperatureUnits
): String {
    val textEndSymbol = when (temperatureUnit) {
        TemperatureUnits.CELSIUS -> " \u2103"
        TemperatureUnits.FAHRENHEIT -> " \u2109"
    }
    return this + textEndSymbol
}

fun String.toWindSpeedUnitString(
    windSpeedUnits: WindSpeedUnits
): String {
    val textEndSymbol = when (windSpeedUnits) {
        WindSpeedUnits.KMH -> " km/h"
        WindSpeedUnits.MS -> " m/s"
        WindSpeedUnits.MPH -> " mph"
        WindSpeedUnits.KN -> " kn"
    }
    return this + textEndSymbol
}

fun String.toPressureUnitString(): String {
    val textEndSymbol = " hPa"
    return this + textEndSymbol
}

fun String.toHumidityUnitString(): String {
    val textEndSymbol = " %"
    return this + textEndSymbol
}
