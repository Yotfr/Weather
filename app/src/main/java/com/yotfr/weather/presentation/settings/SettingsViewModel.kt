package com.yotfr.weather.presentation.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.weather.domain.usecases.GetTemperatureUnitUseCase
import com.yotfr.weather.domain.usecases.GetWindSpeedUnitUseCase
import com.yotfr.weather.domain.usecases.UpdateTemperatureUnitUseCase
import com.yotfr.weather.domain.usecases.UpdateWindSpeedUnitsUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val getTemperatureUnitUseCase: GetTemperatureUnitUseCase,
    private val getWindSpeedUnitUseCase: GetWindSpeedUnitUseCase,
    private val updateTemperatureUnitUseCase: UpdateTemperatureUnitUseCase,
    private val updateWindSpeedUnitsUseCase: UpdateWindSpeedUnitsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getTemperatureUnitUseCase().combine(
                getWindSpeedUnitUseCase()
            ) { temperature, windSpeed ->
                _state.update { state ->
                    state.copy(
                        temperatureUnits = temperature,
                        windSpeedUnits = windSpeed
                    )
                }
            }.collect()
        }
    }

    fun onEvent(event: SettingsEvent) {
        when(event) {
            is SettingsEvent.TemperatureUnitsChanged -> {
                Log.d("TEST","event with ${event.newTemperatureUnit}")
                viewModelScope.launch {
                    updateTemperatureUnitUseCase(
                        temperatureUnit = event.newTemperatureUnit
                    )
                }
            }
            is SettingsEvent.WindSpeedUnitsChanged -> {
                Log.d("TEST","event with ${event.newWindSpeedUnits}")
                viewModelScope.launch {
                    updateWindSpeedUnitsUseCase(
                        windSpeedUnits = event.newWindSpeedUnits
                    )
                }
            }

        }
    }
}