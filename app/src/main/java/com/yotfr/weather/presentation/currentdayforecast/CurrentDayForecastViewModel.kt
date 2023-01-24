package com.yotfr.weather.presentation.currentdayforecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.usecases.GetWeatherInfoForFavoritePlace
import com.yotfr.weather.domain.util.Response
import com.yotfr.weather.presentation.utils.getIconRes
import com.yotfr.weather.presentation.utils.getWeatherDescStringRes
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

    private val _currentSelectedPlaceId = MutableStateFlow(-2L)
    val currentSelectedPlaceId = _currentSelectedPlaceId.asStateFlow()

    init {
        viewModelScope.launch {
            _currentSelectedPlaceId.collectLatest { placeId ->
                getWeatherInfoForFavoritePlace(
                    favoritePlaceId = placeId
                ).collectLatest { response ->
                    when (response) {
                        is Response.Loading -> {
                            if (response.data == null) {
                                processLoadingStateWithoutData()
                            } else {
                                processLoadingStateWithData(response.data as FavoritePlaceInfo)
                            }
                        }
                        is Response.Success -> {
                            if (response.data != null) {
                                processSuccessState(response.data as FavoritePlaceInfo)
                            }
                        }
                        is Response.Exception -> {
                            // TODO exception state
                        }
                    }
                }
            }
        }
    }

    private fun processSuccessState(data: FavoritePlaceInfo) {
        data.weatherInfo?.let { weatherInfo ->
            _state.update { state ->
                state.copy(
                    isLoading = true,
                    currentTime = weatherInfo.currentWeatherData.time.format(
                        DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                    ),
                    toolbarTitle = data.placeName,
                    currentWeatherTypeIconRes = weatherInfo.currentWeatherData.weatherType.getIconRes(),
                    currentWeatherTypeDescription = weatherInfo.currentWeatherData.weatherType.getWeatherDescStringRes(),
                    currentTemperature = weatherInfo.currentWeatherData.temperature.toString(),
                    currentHumidity = weatherInfo.currentWeatherData.humidity.toString(),
                    currentPressure = weatherInfo.currentWeatherData.pressure.toString(),
                    currentWindSpeed = weatherInfo.currentWeatherData.windSpeed.toString(),
                    currentApparentTemperature = weatherInfo.currentWeatherData.apparentTemperature.toString(),
                    sunriseTime = weatherInfo.todaySunrise.format(
                        DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                    ),
                    sunsetTime = weatherInfo.todaySunset.format(
                        DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                    ),
                    hourlyWeatherList = weatherInfo.fromCurrentTimeHourlyWeatherData
                )
            }
        } ?: kotlin.run {
            throw IllegalArgumentException("Weather info cannot be null in loading or success state")
        }
    }

    private fun processLoadingStateWithData(data: FavoritePlaceInfo) {
        data.weatherInfo?.let { weatherInfo ->
            _state.update { state ->
                state.copy(
                    isLoading = true,
                    currentTime = weatherInfo.currentWeatherData.time.format(
                        DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                    ),
                    toolbarTitle = data.placeName,
                    currentWeatherTypeIconRes = weatherInfo.currentWeatherData.weatherType.getIconRes(),
                    currentWeatherTypeDescription = weatherInfo.currentWeatherData.weatherType.getWeatherDescStringRes(),
                    currentTemperature = weatherInfo.currentWeatherData.temperature.toString(),
                    currentHumidity = weatherInfo.currentWeatherData.humidity.toString(),
                    currentPressure = weatherInfo.currentWeatherData.pressure.toString(),
                    currentWindSpeed = weatherInfo.currentWeatherData.windSpeed.toString(),
                    currentApparentTemperature = weatherInfo.currentWeatherData.apparentTemperature.toString(),
                    sunriseTime = weatherInfo.todaySunrise.format(
                        DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                    ),
                    sunsetTime = weatherInfo.todaySunset.format(
                        DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                    ),
                    hourlyWeatherList = weatherInfo.fromCurrentTimeHourlyWeatherData
                )
            }
        } ?: kotlin.run {
            throw IllegalArgumentException("Weather info cannot be null in loading or success state")
        }
    }

    private fun processLoadingStateWithoutData() {
        _state.update { state ->
            state.copy(
                isLoading = true
            )
        }
    }

    fun onEvent(event: CurrentDayForecastEvent) {
        when (event) {
            is CurrentDayForecastEvent.ChangeCurrentSelectedPlaceId -> {
                _currentSelectedPlaceId.value = event.newPlaceId
            }
        }
    }
}
