package com.yotfr.weather.presentation.sevendaysforecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.model.TemperatureUnits
import com.yotfr.weather.domain.usecases.GetFavoritePlaceWIthWeatherCache
import com.yotfr.weather.domain.usecases.GetMeasuringUnitsUseCase
import com.yotfr.weather.domain.util.Response
import com.yotfr.weather.presentation.utils.getIconRes
import com.yotfr.weather.presentation.utils.toTemperatureUnitString
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class SevenDaysForecastViewModel @Inject constructor(
    private val getFavoritePlaceWIthWeatherCache: GetFavoritePlaceWIthWeatherCache,
    private val getMeasuringUnitsUseCase: GetMeasuringUnitsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SevenDaysForecastState())
    val state = _state.asStateFlow()

    private val _selectedIndex = MutableStateFlow(0)
    val selectedIndex = _selectedIndex.asStateFlow()

    private val _selectedPlaceId = MutableStateFlow(-2L)

    init {
        getWeather()
    }

    private fun getWeather() {
        viewModelScope.launch {
            _selectedPlaceId.collectLatest { placeId ->
                combine(
                    getFavoritePlaceWIthWeatherCache(
                        favoritePlaceId = placeId
                    ),
                    getMeasuringUnitsUseCase(),
                    _selectedIndex
                ) { weatherResponse, measuringUnits, selectedIndex ->
                    when (weatherResponse) {
                        is Response.Loading -> {
                            if (weatherResponse.data == null) {
                                processLoadingStateWithoutData()
                            } else {
                                processLoadingStateWithData(
                                    weatherResponse.data as FavoritePlaceInfo,
                                    selectedIndex,
                                    temperatureUnit = measuringUnits.temperatureUnit
                                )
                            }
                        }
                        is Response.Success -> {
                            if (weatherResponse.data != null) {
                                processSuccessState(
                                    weatherResponse.data as FavoritePlaceInfo,
                                    selectedIndex,
                                    temperatureUnit = measuringUnits.temperatureUnit
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

    private fun processLoadingStateWithoutData() {
        _state.update { state ->
            state.copy(
                isLoading = true
            )
        }
    }

    private fun processLoadingStateWithData(
        data: FavoritePlaceInfo,
        index: Int,
        temperatureUnit: TemperatureUnits
    ) {
        data.weatherInfo?.let { weatherInfo ->
            _state.update { state ->
                state.copy(
                    isLoading = true,
                    toolbarTitle = data.placeName,
                    selectedDate = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.date?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    selectedMaxTemperature = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.maxTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    selectedMinTemperature = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.minTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    selectedWeatherType = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.weatherType?.getIconRes(),
                    sunriseTime = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.sunrise?.format(
                            DateTimeFormatter.ofPattern(
                                "HH:mm",
                                Locale.getDefault()
                            )
                        ),
                    sunsetTime = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.sunset?.format(
                            DateTimeFormatter.ofPattern(
                                "HH:mm",
                                Locale.getDefault()
                            )
                        ),
                    selectedDayHourlyWeatherList = weatherInfo
                        .completeWeatherData[index]
                        ?.hourlyWeatherData,
                    todayDate = weatherInfo
                        .completeWeatherData[0]
                        ?.dailyWeatherData
                        ?.date?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    todayMaxTemperature = weatherInfo
                        .completeWeatherData[0]
                        ?.dailyWeatherData?.maxTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    todayMinTemperature = weatherInfo
                        .completeWeatherData[0]
                        ?.dailyWeatherData?.minTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    todayWeatherType = weatherInfo
                        .completeWeatherData[0]
                        ?.dailyWeatherData?.weatherType?.getIconRes(),
                    tomorrowDate = weatherInfo
                        .completeWeatherData[1]
                        ?.dailyWeatherData
                        ?.date?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    tomorrowMaxTemperature = weatherInfo
                        .completeWeatherData[1]
                        ?.dailyWeatherData?.maxTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    tomorrowMinTemperature = weatherInfo
                        .completeWeatherData[1]
                        ?.dailyWeatherData?.minTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    tomorrowWeatherType = weatherInfo
                        .completeWeatherData[1]
                        ?.dailyWeatherData?.weatherType?.getIconRes(),
                    afterTomorrowDate = weatherInfo
                        .completeWeatherData[2]
                        ?.dailyWeatherData
                        ?.date?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    afterTomorrowMaxTemperature = weatherInfo
                        .completeWeatherData[2]
                        ?.dailyWeatherData?.maxTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    afterTomorrowMinTemperature = weatherInfo
                        .completeWeatherData[2]
                        ?.dailyWeatherData?.minTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    afterTomorrowWeatherType = weatherInfo
                        .completeWeatherData[2]
                        ?.dailyWeatherData?.weatherType?.getIconRes(),
                    afterTomorrowDayOfWeek = weatherInfo
                        .completeWeatherData[2]
                        ?.dailyWeatherData?.date?.dayOfWeek.toString(),
                    inTwoDaysDate = weatherInfo
                        .completeWeatherData[3]
                        ?.dailyWeatherData
                        ?.date?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    inTwoDaysMaxTemperature = weatherInfo
                        .completeWeatherData[3]
                        ?.dailyWeatherData?.maxTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    inTwoDaysMinTemperature = weatherInfo
                        .completeWeatherData[3]
                        ?.dailyWeatherData?.minTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    inTwoDaysWeatherType = weatherInfo
                        .completeWeatherData[3]
                        ?.dailyWeatherData?.weatherType?.getIconRes(),
                    inTwoDaysDayOfWeek = weatherInfo
                        .completeWeatherData[3]
                        ?.dailyWeatherData?.date?.dayOfWeek.toString(),
                    inThreeDaysDate = weatherInfo
                        .completeWeatherData[4]
                        ?.dailyWeatherData
                        ?.date?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    inThreeDaysMaxTemperature = weatherInfo
                        .completeWeatherData[4]
                        ?.dailyWeatherData?.maxTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    inThreeDaysMinTemperature = weatherInfo
                        .completeWeatherData[4]
                        ?.dailyWeatherData?.minTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    inThreeDaysWeatherType = weatherInfo
                        .completeWeatherData[4]
                        ?.dailyWeatherData?.weatherType?.getIconRes(),
                    inThreeDaysDayOfWeek = weatherInfo
                        .completeWeatherData[4]
                        ?.dailyWeatherData?.date?.dayOfWeek.toString(),
                    inFourDaysDate = weatherInfo
                        .completeWeatherData[5]
                        ?.dailyWeatherData
                        ?.date?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    inFourDaysMaxTemperature = weatherInfo
                        .completeWeatherData[5]
                        ?.dailyWeatherData?.maxTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    inFourDaysMinTemperature = weatherInfo
                        .completeWeatherData[5]
                        ?.dailyWeatherData?.minTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    inFourDaysWeatherType = weatherInfo
                        .completeWeatherData[5]
                        ?.dailyWeatherData?.weatherType?.getIconRes(),
                    inFourDaysDayOfWeek = weatherInfo
                        .completeWeatherData[5]
                        ?.dailyWeatherData?.date?.dayOfWeek.toString(),
                    inFiveDaysDate = weatherInfo
                        .completeWeatherData[6]
                        ?.dailyWeatherData
                        ?.date?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    inFiveDaysMaxTemperature = weatherInfo
                        .completeWeatherData[6]
                        ?.dailyWeatherData?.maxTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    inFiveDaysMinTemperature = weatherInfo
                        .completeWeatherData[6]
                        ?.dailyWeatherData?.minTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    inFiveDaysWeatherType = weatherInfo
                        .completeWeatherData[6]
                        ?.dailyWeatherData?.weatherType?.getIconRes(),
                    inFiveDaysDayOfWeek = weatherInfo
                        .completeWeatherData[6]
                        ?.dailyWeatherData?.date?.dayOfWeek.toString(),
                    temperatureUnit = temperatureUnit
                )
            }
        } ?: run {
            throw IllegalArgumentException("Weather info cannot be null in loading or success state")
        }
    }

    private fun processSuccessState(
        data: FavoritePlaceInfo,
        index: Int,
        temperatureUnit: TemperatureUnits
    ) {
        data.weatherInfo?.let { weatherInfo ->
            _state.update { state ->
                state.copy(
                    isLoading = false,
                    toolbarTitle = data.placeName,
                    selectedDate = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.date?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    selectedMaxTemperature = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.maxTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    selectedMinTemperature = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.minTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    selectedWeatherType = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.weatherType?.getIconRes(),
                    sunriseTime = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.sunrise?.format(
                            DateTimeFormatter.ofPattern(
                                "HH:mm",
                                Locale.getDefault()
                            )
                        ),
                    sunsetTime = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.sunset?.format(
                            DateTimeFormatter.ofPattern(
                                "HH:mm",
                                Locale.getDefault()
                            )
                        ),
                    selectedDayHourlyWeatherList = weatherInfo
                        .completeWeatherData[index]
                        ?.hourlyWeatherData,
                    todayDate = weatherInfo
                        .completeWeatherData[0]
                        ?.dailyWeatherData
                        ?.date?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    todayMaxTemperature = weatherInfo
                        .completeWeatherData[0]
                        ?.dailyWeatherData?.maxTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    todayMinTemperature = weatherInfo
                        .completeWeatherData[0]
                        ?.dailyWeatherData?.minTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    todayWeatherType = weatherInfo
                        .completeWeatherData[0]
                        ?.dailyWeatherData?.weatherType?.getIconRes(),
                    tomorrowDate = weatherInfo
                        .completeWeatherData[1]
                        ?.dailyWeatherData
                        ?.date?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    tomorrowMaxTemperature = weatherInfo
                        .completeWeatherData[1]
                        ?.dailyWeatherData?.maxTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    tomorrowMinTemperature = weatherInfo
                        .completeWeatherData[1]
                        ?.dailyWeatherData?.minTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    tomorrowWeatherType = weatherInfo
                        .completeWeatherData[1]
                        ?.dailyWeatherData?.weatherType?.getIconRes(),
                    afterTomorrowDate = weatherInfo
                        .completeWeatherData[2]
                        ?.dailyWeatherData
                        ?.date?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    afterTomorrowMaxTemperature = weatherInfo
                        .completeWeatherData[2]
                        ?.dailyWeatherData?.maxTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    afterTomorrowMinTemperature = weatherInfo
                        .completeWeatherData[2]
                        ?.dailyWeatherData?.minTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    afterTomorrowWeatherType = weatherInfo
                        .completeWeatherData[2]
                        ?.dailyWeatherData?.weatherType?.getIconRes(),
                    afterTomorrowDayOfWeek = weatherInfo
                        .completeWeatherData[2]
                        ?.dailyWeatherData?.date?.dayOfWeek.toString(),
                    inTwoDaysDate = weatherInfo
                        .completeWeatherData[3]
                        ?.dailyWeatherData
                        ?.date?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    inTwoDaysMaxTemperature = weatherInfo
                        .completeWeatherData[3]
                        ?.dailyWeatherData?.maxTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    inTwoDaysMinTemperature = weatherInfo
                        .completeWeatherData[3]
                        ?.dailyWeatherData?.minTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    inTwoDaysWeatherType = weatherInfo
                        .completeWeatherData[3]
                        ?.dailyWeatherData?.weatherType?.getIconRes(),
                    inTwoDaysDayOfWeek = weatherInfo
                        .completeWeatherData[3]
                        ?.dailyWeatherData?.date?.dayOfWeek.toString(),
                    inThreeDaysDate = weatherInfo
                        .completeWeatherData[4]
                        ?.dailyWeatherData
                        ?.date?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    inThreeDaysMaxTemperature = weatherInfo
                        .completeWeatherData[4]
                        ?.dailyWeatherData?.maxTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    inThreeDaysMinTemperature = weatherInfo
                        .completeWeatherData[4]
                        ?.dailyWeatherData?.minTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    inThreeDaysWeatherType = weatherInfo
                        .completeWeatherData[4]
                        ?.dailyWeatherData?.weatherType?.getIconRes(),
                    inThreeDaysDayOfWeek = weatherInfo
                        .completeWeatherData[4]
                        ?.dailyWeatherData?.date?.dayOfWeek.toString(),
                    inFourDaysDate = weatherInfo
                        .completeWeatherData[5]
                        ?.dailyWeatherData
                        ?.date?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    inFourDaysMaxTemperature = weatherInfo
                        .completeWeatherData[5]
                        ?.dailyWeatherData?.maxTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    inFourDaysMinTemperature = weatherInfo
                        .completeWeatherData[5]
                        ?.dailyWeatherData?.minTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    inFourDaysWeatherType = weatherInfo
                        .completeWeatherData[5]
                        ?.dailyWeatherData?.weatherType?.getIconRes(),
                    inFourDaysDayOfWeek = weatherInfo
                        .completeWeatherData[5]
                        ?.dailyWeatherData?.date?.dayOfWeek.toString(),
                    inFiveDaysDate = weatherInfo
                        .completeWeatherData[6]
                        ?.dailyWeatherData
                        ?.date?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    inFiveDaysMaxTemperature = weatherInfo
                        .completeWeatherData[6]
                        ?.dailyWeatherData?.maxTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    inFiveDaysMinTemperature = weatherInfo
                        .completeWeatherData[6]
                        ?.dailyWeatherData?.minTemperature.toString().toTemperatureUnitString(
                            temperatureUnit = temperatureUnit
                        ),
                    inFiveDaysWeatherType = weatherInfo
                        .completeWeatherData[6]
                        ?.dailyWeatherData?.weatherType?.getIconRes(),
                    inFiveDaysDayOfWeek = weatherInfo
                        .completeWeatherData[6]
                        ?.dailyWeatherData?.date?.dayOfWeek.toString(),
                    temperatureUnit = temperatureUnit
                )
            }
        } ?: run {
            throw IllegalArgumentException("Weather info cannot be null in loading or success state")
        }
    }

    fun onEvent(event: SevenDaysForecastEvent) {
        when (event) {
            is SevenDaysForecastEvent.SelectedDayIndexChanged -> {
                _selectedIndex.value = event.newIndex
            }
            is SevenDaysForecastEvent.ChangeCurrentSelectedPlaceId -> {
                _selectedPlaceId.value = event.newPlaceId
            }
            SevenDaysForecastEvent.Swiped -> {
                getWeather()
            }
        }
    }
}
