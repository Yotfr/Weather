package com.yotfr.weather.presentation.currentdayforecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.model.TemperatureUnits
import com.yotfr.weather.domain.model.WindSpeedUnits
import com.yotfr.weather.domain.usecases.GetFavoritePlaceWIthWeatherCache
import com.yotfr.weather.domain.usecases.GetMeasuringUnitsUseCase
import com.yotfr.weather.domain.util.Response
import com.yotfr.weather.presentation.utils.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class CurrentDayForecastViewModel @Inject constructor(
    private val getFavoritePlaceWIthWeatherCache: GetFavoritePlaceWIthWeatherCache,
    private val getMeasuringUnitsUseCase: GetMeasuringUnitsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CurrentDayForecastState())
    val state = _state.asStateFlow()

    private val _currentSelectedPlaceId = MutableStateFlow(-2L)
    val currentSelectedPlaceId = _currentSelectedPlaceId.asStateFlow()

    init {
        getWeather()
    }

    private fun getWeather() {
        viewModelScope.launch {
            _currentSelectedPlaceId.collectLatest { placeId ->
                combine(
                    getMeasuringUnitsUseCase(),
                    getFavoritePlaceWIthWeatherCache(
                        favoritePlaceId = placeId
                    )
                ) { measuringUnits, weatherResponse ->
                    when (weatherResponse) {
                        is Response.Loading -> {
                            if (weatherResponse.data == null) {
                                processLoadingStateWithoutData()
                            } else {
                                processLoadingStateWithData(
                                    weatherResponse.data as FavoritePlaceInfo,
                                    temperatureUnit = measuringUnits.temperatureUnit,
                                    windSpeedUnit = measuringUnits.windSpeedUnit
                                )
                            }
                        }
                        is Response.Success -> {
                            if (weatherResponse.data != null) {
                                processSuccessState(
                                    weatherResponse.data as FavoritePlaceInfo,
                                    temperatureUnit = measuringUnits.temperatureUnit,
                                    windSpeedUnit = measuringUnits.windSpeedUnit
                                )
                            }
                        }
                        is Response.Exception -> {
                            // TODO exception state
                        }
                    }
                }.collect()
            }
        }
    }

    private fun processSuccessState(
        data: FavoritePlaceInfo,
        temperatureUnit: TemperatureUnits,
        windSpeedUnit: WindSpeedUnits
    ) {
        data.weatherInfo?.let { weatherInfo ->
            _state.update { state ->
                state.copy(
                    isLoading = false,
                    currentTime = weatherInfo.currentWeatherData.time.format(
                        DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                    ),
                    toolbarTitle = data.placeName,
                    currentWeatherTypeIconRes = weatherInfo.currentWeatherData.weatherType.getIconRes(),
                    currentWeatherTypeDescription = weatherInfo.currentWeatherData.weatherType.getWeatherDescStringRes(),
                    currentTemperature = weatherInfo.currentWeatherData.temperature.toString()
                        .toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    currentHumidity = weatherInfo.currentWeatherData.humidity.toString()
                        .toHumidityUnitString(),
                    currentPressure = weatherInfo.currentWeatherData.pressure.toString()
                        .toPressureUnitString(),
                    currentWindSpeed = weatherInfo.currentWeatherData.windSpeed.toString()
                        .toWindSpeedUnitString(
                            windSpeedUnits = windSpeedUnit
                        ),
                    currentApparentTemperature = weatherInfo.currentWeatherData.apparentTemperature.toString()
                        .toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    sunriseTime = weatherInfo.todaySunrise.format(
                        DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                    ),
                    sunsetTime = weatherInfo.todaySunset.format(
                        DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                    ),
                    hourlyWeatherList = weatherInfo.fromCurrentTimeHourlyWeatherData,
                    temperatureUnits = temperatureUnit
                )
            }
        } ?: kotlin.run {
            throw IllegalArgumentException("Weather info cannot be null in loading or success state")
        }
    }

    private fun processLoadingStateWithData(
        data: FavoritePlaceInfo,
        temperatureUnit: TemperatureUnits,
        windSpeedUnit: WindSpeedUnits
    ) {
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
                    currentTemperature = weatherInfo.currentWeatherData.temperature.toString()
                        .toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    currentHumidity = weatherInfo.currentWeatherData.humidity.toString()
                        .toHumidityUnitString(),
                    currentPressure = weatherInfo.currentWeatherData.pressure.toString()
                        .toPressureUnitString(),
                    currentWindSpeed = weatherInfo.currentWeatherData.windSpeed.toString()
                        .toWindSpeedUnitString(
                            windSpeedUnits = windSpeedUnit
                        ),
                    currentApparentTemperature = weatherInfo.currentWeatherData.apparentTemperature.toString()
                        .toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    sunriseTime = weatherInfo.todaySunrise.format(
                        DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                    ),
                    sunsetTime = weatherInfo.todaySunset.format(
                        DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                    ),
                    hourlyWeatherList = weatherInfo.fromCurrentTimeHourlyWeatherData,
                    temperatureUnits = temperatureUnit
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
            CurrentDayForecastEvent.Swiped -> {
                getWeather()
            }
        }
    }
}
