package com.yotfr.weather.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.weather.domain.usecases.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val getMeasuringUnitsUseCase: GetMeasuringUnitsUseCase,
    private val updateTemperatureUnitUseCase: UpdateTemperatureUnitUseCase,
    private val updateWindSpeedUnitsUseCase: UpdateWindSpeedUnitsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getMeasuringUnitsUseCase().collectLatest { measuringUnits ->
                _state.update { state ->
                    state.copy(
                        temperatureUnits = measuringUnits.temperatureUnit,
                        windSpeedUnits = measuringUnits.windSpeedUnit
                    )
                }
            }
        }
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.TemperatureUnitsChanged -> {
                viewModelScope.launch {
                    updateTemperatureUnitUseCase(
                        temperatureUnit = event.newTemperatureUnit
                    )
                }
            }
            is SettingsEvent.WindSpeedUnitsChanged -> {
                viewModelScope.launch {
                    updateWindSpeedUnitsUseCase(
                        windSpeedUnits = event.newWindSpeedUnits
                    )
                }
            }
        }
    }
}
