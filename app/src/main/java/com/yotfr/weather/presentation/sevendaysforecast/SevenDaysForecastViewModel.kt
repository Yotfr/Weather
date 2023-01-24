package com.yotfr.weather.presentation.sevendaysforecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.usecases.GetWeatherInfoForFavoritePlace
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class SevenDaysForecastViewModel @Inject constructor(
    private val getWeatherInfoForFavoritePlace: GetWeatherInfoForFavoritePlace
) : ViewModel() {

    private val _state = MutableStateFlow(SevenDaysForecastState())
    val state = _state.asStateFlow()

    private val _selectedIndex = MutableStateFlow(0)
    val selectedIndex = _selectedIndex.asStateFlow()

    private val _selectedPlaceId = MutableStateFlow(-2L)

    init {
        viewModelScope.launch {
            _selectedPlaceId.collectLatest { placeId ->
                getWeatherInfoForFavoritePlace(
                    favoritePlaceId = placeId
                ).combine(
                    _selectedIndex
                ) { response, index ->
                    when (response) {
                        is Response.Loading -> {
                            if (response.data == null) {
                                processLoadingStateWithoutData()
                            } else {
                                processLoadingStateWithData(response.data as FavoritePlaceInfo, index)
                            }
                        }
                        is Response.Success -> {
                            if (response.data != null) {
                                processSuccessState(response.data as FavoritePlaceInfo, index)
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

    private fun processLoadingStateWithData(data: FavoritePlaceInfo, index: Int) {
        data.weatherInfo?.let { weatherInfo ->
            _state.update { state ->
                state.copy(
                    isLoading = false,
                    toolbarTitle = data.placeName,
                    selectedDate = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.time?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    selectedMaxTemperature = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.maxTemperature.toString(),
                    selectedMinTemperature = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.minTemperature.toString(),
                    selectedWeatherType = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.weatherType?.iconRes,
                    sunriseTime = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.sunrise?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    sunsetTime = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.sunset?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    selectedDayHourlyWeatherList = weatherInfo
                        .completeWeatherData[index]
                        ?.hourlyWeatherData,
                    todayDate = weatherInfo
                        .completeWeatherData[0]
                        ?.dailyWeatherData
                        ?.time?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    todayMaxTemperature = weatherInfo
                        .completeWeatherData[0]
                        ?.dailyWeatherData?.maxTemperature.toString(),
                    todayMinTemperature = weatherInfo
                        .completeWeatherData[0]
                        ?.dailyWeatherData?.minTemperature.toString(),
                    todayWeatherType = weatherInfo
                        .completeWeatherData[0]
                        ?.dailyWeatherData?.weatherType?.iconRes,
                    tomorrowDate = weatherInfo
                        .completeWeatherData[1]
                        ?.dailyWeatherData
                        ?.time?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    tomorrowMaxTemperature = weatherInfo
                        .completeWeatherData[1]
                        ?.dailyWeatherData?.maxTemperature.toString(),
                    tomorrowMinTemperature = weatherInfo
                        .completeWeatherData[1]
                        ?.dailyWeatherData?.minTemperature.toString(),
                    tomorrowWeatherType = weatherInfo
                        .completeWeatherData[1]
                        ?.dailyWeatherData?.weatherType?.iconRes,
                    afterTomorrowDate = weatherInfo
                        .completeWeatherData[2]
                        ?.dailyWeatherData
                        ?.time?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    afterTomorrowMaxTemperature = weatherInfo
                        .completeWeatherData[2]
                        ?.dailyWeatherData?.maxTemperature.toString(),
                    afterTomorrowMinTemperature = weatherInfo
                        .completeWeatherData[2]
                        ?.dailyWeatherData?.minTemperature.toString(),
                    afterTomorrowWeatherType = weatherInfo
                        .completeWeatherData[2]
                        ?.dailyWeatherData?.weatherType?.iconRes,
                    afterTomorrowDayOfWeek = weatherInfo
                        .completeWeatherData[2]
                        ?.dailyWeatherData?.time?.dayOfWeek.toString(),
                    inTwoDaysDate = weatherInfo
                        .completeWeatherData[3]
                        ?.dailyWeatherData
                        ?.time?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    inTwoDaysMaxTemperature = weatherInfo
                        .completeWeatherData[3]
                        ?.dailyWeatherData?.maxTemperature.toString(),
                    inTwoDaysMinTemperature = weatherInfo
                        .completeWeatherData[3]
                        ?.dailyWeatherData?.minTemperature.toString(),
                    inTwoDaysWeatherType = weatherInfo
                        .completeWeatherData[3]
                        ?.dailyWeatherData?.weatherType?.iconRes,
                    inTwoDaysDayOfWeek = weatherInfo
                        .completeWeatherData[3]
                        ?.dailyWeatherData?.time?.dayOfWeek.toString(),
                    inThreeDaysDate = weatherInfo
                        .completeWeatherData[4]
                        ?.dailyWeatherData
                        ?.time?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    inThreeDaysMaxTemperature = weatherInfo
                        .completeWeatherData[4]
                        ?.dailyWeatherData?.maxTemperature.toString(),
                    inThreeDaysMinTemperature = weatherInfo
                        .completeWeatherData[4]
                        ?.dailyWeatherData?.minTemperature.toString(),
                    inThreeDaysWeatherType = weatherInfo
                        .completeWeatherData[4]
                        ?.dailyWeatherData?.weatherType?.iconRes,
                    inThreeDaysDayOfWeek = weatherInfo
                        .completeWeatherData[4]
                        ?.dailyWeatherData?.time?.dayOfWeek.toString(),
                    inFourDaysDate = weatherInfo
                        .completeWeatherData[5]
                        ?.dailyWeatherData
                        ?.time?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    inFourDaysMaxTemperature = weatherInfo
                        .completeWeatherData[5]
                        ?.dailyWeatherData?.maxTemperature.toString(),
                    inFourDaysMinTemperature = weatherInfo
                        .completeWeatherData[5]
                        ?.dailyWeatherData?.minTemperature.toString(),
                    inFourDaysWeatherType = weatherInfo
                        .completeWeatherData[5]
                        ?.dailyWeatherData?.weatherType?.iconRes,
                    inFourDaysDayOfWeek = weatherInfo
                        .completeWeatherData[5]
                        ?.dailyWeatherData?.time?.dayOfWeek.toString(),
                    inFiveDaysDate = weatherInfo
                        .completeWeatherData[6]
                        ?.dailyWeatherData
                        ?.time?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    inFiveDaysMaxTemperature = weatherInfo
                        .completeWeatherData[6]
                        ?.dailyWeatherData?.maxTemperature.toString(),
                    inFiveDaysMinTemperature = weatherInfo
                        .completeWeatherData[6]
                        ?.dailyWeatherData?.minTemperature.toString(),
                    inFiveDaysWeatherType = weatherInfo
                        .completeWeatherData[6]
                        ?.dailyWeatherData?.weatherType?.iconRes,
                    inFiveDaysDayOfWeek = weatherInfo
                        .completeWeatherData[6]
                        ?.dailyWeatherData?.time?.dayOfWeek.toString()
                )
            }
        } ?: run {
            throw IllegalArgumentException("Weather info cannot be null in loading or success state")
        }
    }

    private fun processSuccessState(data: FavoritePlaceInfo, index: Int) {
        data.weatherInfo?.let { weatherInfo ->
            _state.update { state ->
                state.copy(
                    isLoading = false,
                    toolbarTitle = data.placeName,
                    selectedDate = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.time?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    selectedMaxTemperature = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.maxTemperature.toString(),
                    selectedMinTemperature = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.minTemperature.toString(),
                    selectedWeatherType = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.weatherType?.iconRes,
                    sunriseTime = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.sunrise?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    sunsetTime = weatherInfo
                        .completeWeatherData[index]
                        ?.dailyWeatherData
                        ?.sunset?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    selectedDayHourlyWeatherList = weatherInfo
                        .completeWeatherData[index]
                        ?.hourlyWeatherData,
                    todayDate = weatherInfo
                        .completeWeatherData[0]
                        ?.dailyWeatherData
                        ?.time?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    todayMaxTemperature = weatherInfo
                        .completeWeatherData[0]
                        ?.dailyWeatherData?.maxTemperature.toString(),
                    todayMinTemperature = weatherInfo
                        .completeWeatherData[0]
                        ?.dailyWeatherData?.minTemperature.toString(),
                    todayWeatherType = weatherInfo
                        .completeWeatherData[0]
                        ?.dailyWeatherData?.weatherType?.iconRes,
                    tomorrowDate = weatherInfo
                        .completeWeatherData[1]
                        ?.dailyWeatherData
                        ?.time?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    tomorrowMaxTemperature = weatherInfo
                        .completeWeatherData[1]
                        ?.dailyWeatherData?.maxTemperature.toString(),
                    tomorrowMinTemperature = weatherInfo
                        .completeWeatherData[1]
                        ?.dailyWeatherData?.minTemperature.toString(),
                    tomorrowWeatherType = weatherInfo
                        .completeWeatherData[1]
                        ?.dailyWeatherData?.weatherType?.iconRes,
                    afterTomorrowDate = weatherInfo
                        .completeWeatherData[2]
                        ?.dailyWeatherData
                        ?.time?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    afterTomorrowMaxTemperature = weatherInfo
                        .completeWeatherData[2]
                        ?.dailyWeatherData?.maxTemperature.toString(),
                    afterTomorrowMinTemperature = weatherInfo
                        .completeWeatherData[2]
                        ?.dailyWeatherData?.minTemperature.toString(),
                    afterTomorrowWeatherType = weatherInfo
                        .completeWeatherData[2]
                        ?.dailyWeatherData?.weatherType?.iconRes,
                    afterTomorrowDayOfWeek = weatherInfo
                        .completeWeatherData[2]
                        ?.dailyWeatherData?.time?.dayOfWeek.toString(),
                    inTwoDaysDate = weatherInfo
                        .completeWeatherData[3]
                        ?.dailyWeatherData
                        ?.time?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    inTwoDaysMaxTemperature = weatherInfo
                        .completeWeatherData[3]
                        ?.dailyWeatherData?.maxTemperature.toString(),
                    inTwoDaysMinTemperature = weatherInfo
                        .completeWeatherData[3]
                        ?.dailyWeatherData?.minTemperature.toString(),
                    inTwoDaysWeatherType = weatherInfo
                        .completeWeatherData[3]
                        ?.dailyWeatherData?.weatherType?.iconRes,
                    inTwoDaysDayOfWeek = weatherInfo
                        .completeWeatherData[3]
                        ?.dailyWeatherData?.time?.dayOfWeek.toString(),
                    inThreeDaysDate = weatherInfo
                        .completeWeatherData[4]
                        ?.dailyWeatherData
                        ?.time?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    inThreeDaysMaxTemperature = weatherInfo
                        .completeWeatherData[4]
                        ?.dailyWeatherData?.maxTemperature.toString(),
                    inThreeDaysMinTemperature = weatherInfo
                        .completeWeatherData[4]
                        ?.dailyWeatherData?.minTemperature.toString(),
                    inThreeDaysWeatherType = weatherInfo
                        .completeWeatherData[4]
                        ?.dailyWeatherData?.weatherType?.iconRes,
                    inThreeDaysDayOfWeek = weatherInfo
                        .completeWeatherData[4]
                        ?.dailyWeatherData?.time?.dayOfWeek.toString(),
                    inFourDaysDate = weatherInfo
                        .completeWeatherData[5]
                        ?.dailyWeatherData
                        ?.time?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    inFourDaysMaxTemperature = weatherInfo
                        .completeWeatherData[5]
                        ?.dailyWeatherData?.maxTemperature.toString(),
                    inFourDaysMinTemperature = weatherInfo
                        .completeWeatherData[5]
                        ?.dailyWeatherData?.minTemperature.toString(),
                    inFourDaysWeatherType = weatherInfo
                        .completeWeatherData[5]
                        ?.dailyWeatherData?.weatherType?.iconRes,
                    inFourDaysDayOfWeek = weatherInfo
                        .completeWeatherData[5]
                        ?.dailyWeatherData?.time?.dayOfWeek.toString(),
                    inFiveDaysDate = weatherInfo
                        .completeWeatherData[6]
                        ?.dailyWeatherData
                        ?.time?.format(
                            DateTimeFormatter.ofPattern(
                                "MMMM d",
                                Locale.getDefault()
                            )
                        ),
                    inFiveDaysMaxTemperature = weatherInfo
                        .completeWeatherData[6]
                        ?.dailyWeatherData?.maxTemperature.toString(),
                    inFiveDaysMinTemperature = weatherInfo
                        .completeWeatherData[6]
                        ?.dailyWeatherData?.minTemperature.toString(),
                    inFiveDaysWeatherType = weatherInfo
                        .completeWeatherData[6]
                        ?.dailyWeatherData?.weatherType?.iconRes,
                    inFiveDaysDayOfWeek = weatherInfo
                        .completeWeatherData[6]
                        ?.dailyWeatherData?.time?.dayOfWeek.toString()
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
        }
    }
}
