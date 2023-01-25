package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.MeasuringUnits
import com.yotfr.weather.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

/**
 * [GetMeasuringUnitsUseCase] returns currently selected measuring units
 */
class GetMeasuringUnitsUseCase(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<MeasuringUnits> {
        return settingsRepository.getMeasuringUnitsFlow()
    }
}