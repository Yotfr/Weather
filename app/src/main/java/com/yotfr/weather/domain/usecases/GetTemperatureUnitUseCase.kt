package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.TemperatureUnits
import com.yotfr.weather.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetTemperatureUnitUseCase(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<TemperatureUnits> {
        return settingsRepository.getTemperatureUnits()
    }
}