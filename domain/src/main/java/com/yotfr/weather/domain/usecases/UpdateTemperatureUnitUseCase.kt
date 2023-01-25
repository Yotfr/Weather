package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.TemperatureUnits
import com.yotfr.weather.domain.repository.SettingsRepository

/**
 * [UpdateTemperatureUnitUseCase] changes the current temperature unit
 */
class UpdateTemperatureUnitUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(temperatureUnit: TemperatureUnits) {
        settingsRepository.updateTemperatureUnit(
            temperatureUnits = temperatureUnit
        )
    }
}