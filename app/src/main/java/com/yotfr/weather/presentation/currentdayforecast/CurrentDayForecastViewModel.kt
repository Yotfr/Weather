package com.yotfr.weather.presentation.currentdayforecast

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.usecases.GetWeatherInfoForFavoritePlace
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
                    Log.d("TEST","$response")
                    when (response) {
                        is Response.Loading -> {
                            if (response.data == null) {
                                processLoadingStateWithoutData()
                            } else {
                                processLoadingStateWithData(response.data)
                            }
                        }
                        is Response.Success -> {
                            if (response.data != null) {
                                processSuccessState(response.data)
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
        if (data.weatherInfo == null) {
            throw IllegalArgumentException("Weather info cannot be null in loading or success state")
        }
        _state.update { state ->
            state.copy(
                isLoading = true,
                currentTime = data.weatherInfo.currentWeatherData.time.format(
                    DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                ),
                toolbarTitle = data.placeName,
                currentWeatherTypeIconRes = data.weatherInfo.currentWeatherData.weatherType.iconRes,
                currentWeatherTypeDescription = data.weatherInfo.currentWeatherData.weatherType.weatherDesc,
                currentTemperature = data.weatherInfo.currentWeatherData.temperature.toString(),
                currentHumidity = data.weatherInfo.currentWeatherData.humidity.toString(),
                currentPressure = data.weatherInfo.currentWeatherData.pressure.toString(),
                currentWindSpeed = data.weatherInfo.currentWeatherData.windSpeed.toString(),
                currentApparentTemperature = data.weatherInfo.currentWeatherData.apparentTemperature.toString(),
                sunriseTime = data.weatherInfo.todaySunrise.format(
                    DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                ),
                sunsetTime = data.weatherInfo.todaySunset.format(
                    DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                ),
                hourlyWeatherList = data.weatherInfo.fromCurrentTimeHourlyWeatherData
            )
        }
    }

    private fun processLoadingStateWithData(data: FavoritePlaceInfo) {
        if (data.weatherInfo == null) {
            throw IllegalArgumentException("Weather info cannot be null in loading or success state")
        }
        _state.update { state ->
            state.copy(
                isLoading = true,
                currentTime = data.weatherInfo.currentWeatherData.time.format(
                    DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                ),
                toolbarTitle = data.placeName,
                currentWeatherTypeIconRes = data.weatherInfo.currentWeatherData.weatherType.iconRes,
                currentWeatherTypeDescription = data.weatherInfo.currentWeatherData.weatherType.weatherDesc,
                currentTemperature = data.weatherInfo.currentWeatherData.temperature.toString(),
                currentHumidity = data.weatherInfo.currentWeatherData.humidity.toString(),
                currentPressure = data.weatherInfo.currentWeatherData.pressure.toString(),
                currentWindSpeed = data.weatherInfo.currentWeatherData.windSpeed.toString(),
                currentApparentTemperature = data.weatherInfo.currentWeatherData.apparentTemperature.toString(),
                sunriseTime = data.weatherInfo.todaySunrise.format(
                    DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                ),
                sunsetTime = data.weatherInfo.todaySunset.format(
                    DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                ),
                hourlyWeatherList = data.weatherInfo.fromCurrentTimeHourlyWeatherData
            )
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
