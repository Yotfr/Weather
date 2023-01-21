package com.yotfr.weather.presentation.currentdayforecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.weather.domain.usecases.GetWeatherInfoForFavoritePlace
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class CurrentDayForecastViewModel @Inject constructor(
    private val getWeatherInfoForFavoritePlace: GetWeatherInfoForFavoritePlace
) : ViewModel() {

    private val _state = MutableStateFlow(CurrentDayForecastState())
    val state = _state.asStateFlow()

    private val _currentSelectedPlaceId = MutableStateFlow(0L)

    init {
        viewModelScope.launch {
            getWeatherInfoForFavoritePlace(
                favoritePlaceId = _currentSelectedPlaceId.value
            ).collectLatest { placeInfo ->
                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        currentTime = placeInfo.weatherInfo.currentWeatherData.time.format(
                            DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                        ),
                        currentWeatherTypeIconRes = placeInfo.weatherInfo.currentWeatherData.weatherType.iconRes,
                        currentWeatherTypeDescription = placeInfo.weatherInfo.currentWeatherData.weatherType.weatherDesc,
                        currentTemperature = placeInfo.weatherInfo.currentWeatherData.temperature.toString(),
                        currentHumidity = placeInfo.weatherInfo.currentWeatherData.humidity.toString(),
                        currentPressure = placeInfo.weatherInfo.currentWeatherData.pressure.toString(),
                        currentWindSpeed = placeInfo.weatherInfo.currentWeatherData.windSpeed.toString(),
                        currentApparentTemperature = placeInfo.weatherInfo.currentWeatherData.apparentTemperature.toString(),
                        sunriseTime = placeInfo.weatherInfo.todaySunrise.format(
                            DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                        ),
                        sunsetTime = placeInfo.weatherInfo.todaySunset.format(
                            DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                        ),
                        hourlyWeatherList = placeInfo.weatherInfo.fromCurrentTimeHourlyWeatherData
                    )
                }
            }
        }
    }

    fun onEvent(event: CurrentDayForecastEvent) {
        when(event) {
            is CurrentDayForecastEvent.ChangeCurrentSelectedPlaceId -> {
                _currentSelectedPlaceId.value = event.newPlaceId
            }
        }
    }

}
