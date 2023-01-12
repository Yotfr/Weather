package com.yotfr.weather.presentation.weather_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.weather.domain.model.WeatherInfo
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

class WeatherInfoViewModel @Inject constructor(
    private val loadWeatherInfoUseCase: LoadWeatherInfoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherInfoState())
    val state = _state.asStateFlow()

    init {
        loadWeatherData()
    }

    private fun loadWeatherData() {
        viewModelScope.launch {
            loadWeatherInfoUseCase().collectLatest { response ->
                when (response) {
                    is Response.Loading -> {
                        processLoadingState()
                    }
                    is Response.Success -> {
                        processSuccessState(response)
                    }
                    is Response.Error -> {
                    }
                }
            }
        }
    }

    private fun processSuccessState(response: Response<WeatherInfo>) {
        response.data?.currentWeatherData?.let { weatherData ->
            _state.update {
                it.copy(
                    isLoading = false,
                    currentTime = weatherData.time.format(
                        DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                    ),
                    currentWeatherTypeIconRes = weatherData.weatherType.iconRes,
                    currentWeatherTypeDescription = weatherData.weatherType.weatherDesc,
                    currentTemperature = weatherData.temperature.toString(),
                    currentHumidity = weatherData.humidity.toString(),
                    currentPressure = weatherData.pressure.toString(),
                    currentWindSpeed = weatherData.windSpeed.toString(),
                    hourlyWeatherListForToday = response.data.detailedWeatherDataPerDay[0],
                    hourlyWeatherListForTomorrow = response.data.detailedWeatherDataPerDay[1],
                    dayAfterTomorrowDate = response.data.detailedWeatherDataPerDay[2]?.get(0)?.time?.format(
                        DateTimeFormatter.ofPattern("MMMM d", Locale.getDefault())
                    ),
                    hourlyWeatherListForDayAfterTomorrow = response.data.detailedWeatherDataPerDay[2],
                    inTwoDaysDate = response.data.detailedWeatherDataPerDay[3]?.get(0)?.time?.format(
                        DateTimeFormatter.ofPattern("MMMM d", Locale.getDefault())
                    ),
                    hourlyWeatherListForInTwoDays = response.data.detailedWeatherDataPerDay[3],
                    inThreeDaysDate = response.data.detailedWeatherDataPerDay[4]?.get(0)?.time?.format(
                        DateTimeFormatter.ofPattern("MMMM d", Locale.getDefault())
                    ),
                    hourlyWeatherListForInThreeDays = response.data.detailedWeatherDataPerDay[4],
                    inFourDaysDate = response.data.detailedWeatherDataPerDay[5]?.get(0)?.time?.format(
                        DateTimeFormatter.ofPattern("MMMM d", Locale.getDefault())
                    ),
                    hourlyWeatherListForInFourDays = response.data.detailedWeatherDataPerDay[5],
                    inFiveDaysDate = response.data.detailedWeatherDataPerDay[6]?.get(0)?.time?.format(
                        DateTimeFormatter.ofPattern("MMMM d", Locale.getDefault())
                    ),
                    hourlyWeatherListForInFiveDays = response.data.detailedWeatherDataPerDay[6]
                )
            }
        }
    }

    private fun processLoadingState() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
    }
}
