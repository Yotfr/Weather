package com.yotfr.weather.presentation.currentdayforecast

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.weather.domain.usecases.LoadWeatherInfoUseCase
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class CurrentDayForecastViewModel @Inject constructor(
    private val loadWeatherInfoUseCase: LoadWeatherInfoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CurrentDayForecastState())
    val state = _state.asStateFlow()

    init {
        loadWeatherData()
    }

    private fun loadWeatherData() {
        viewModelScope.launch {
            loadWeatherInfoUseCase().collectLatest { response ->
                Log.d("TEST","$response")
                when (response) {
                    is Response.Loading<*> -> {
                        _state.update { state ->
                            state.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Response.Success -> {
                        _state.update { state ->
                            state.copy(
                                isLoading = false,
                                currentTime = response.data.currentWeatherData.time.format(
                                    DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                                ),
                                currentWeatherTypeIconRes = response.data.currentWeatherData.weatherType.iconRes,
                                currentWeatherTypeDescription = response.data.currentWeatherData.weatherType.weatherDesc,
                                currentTemperature = response.data.currentWeatherData.temperature.toString(),
                                currentHumidity = response.data.currentWeatherData.humidity.toString(),
                                currentPressure = response.data.currentWeatherData.pressure.toString(),
                                currentWindSpeed = response.data.currentWeatherData.windSpeed.toString(),
                                currentApparentTemperature = response.data.currentWeatherData.apparentTemperature.toString(),
                                sunriseTime = response.data.todaySunrise.format(
                                    DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                                ),
                                sunsetTime = response.data.todaySunset.format(
                                    DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                                ),
                                hourlyWeatherList = response.data.fromCurrentTimeHourlyWeatherData
                            )
                        }
                    }
                    is Response.Exception -> {
                    }
                }
            }
        }
    }
}
