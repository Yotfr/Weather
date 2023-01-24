package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.WindSpeedUnits
import com.yotfr.weather.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetWindSpeedUnitUseCase(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<WindSpeedUnits> {
        return settingsRepository.getWindSpeedUnits()
    }
}