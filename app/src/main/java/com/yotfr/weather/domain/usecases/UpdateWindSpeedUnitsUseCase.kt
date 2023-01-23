package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.WindSpeedUnits
import com.yotfr.weather.domain.repository.SettingsRepository

class UpdateWindSpeedUnitsUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(windSpeedUnits: WindSpeedUnits) {
        settingsRepository.updateWindSpeedUnits(
            windSpeedUnits = windSpeedUnits
        )
    }
}