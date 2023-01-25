package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.WindSpeedUnits
import com.yotfr.weather.domain.repository.SettingsRepository

/**
 * [UpdateWindSpeedUnitsUseCase] changes the current wind speed unit
 */
class UpdateWindSpeedUnitsUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(windSpeedUnits: WindSpeedUnits) {
        settingsRepository.updateWindSpeedUnit(
            windSpeedUnits = windSpeedUnits
        )
    }
}