package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.TemperatureUnits
import com.yotfr.weather.domain.repository.SettingsRepository

class UpdateTemperatureUnitUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(temperatureUnit: TemperatureUnits) {
        settingsRepository.updateTemperatureUnits(
            temperatureUnits = temperatureUnit
        )
    }
}