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
                    when(response) {
                        is Response.Loading -> {
                            if (response.data == null) {
                                processLoadingStateWithoutData()
                            }else {
                                processLoadingStateWithData(response.data, index)
                            }
                        }
                        is Response.Success -> {
                            if (response.data != null) {
                                processSuccessState(response.data, index)
                            }
                        }
                        is Response.Exception -> {
                            //TODO exception state
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
        if (data.weatherInfo == null) {
            throw IllegalArgumentException("Weather info cannot be null in loading or success state")
        }
        _state.update { state ->
            state.copy(
                isLoading = false,
                toolbarTitle = data.placeName,
                selectedDate = data.weatherInfo
                    .completeWeatherData[index]
                    ?.dailyWeatherData
                    ?.time?.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM d",
                            Locale.getDefault()
                        )
                    ),
                selectedMaxTemperature = data.weatherInfo
                    .completeWeatherData[index]
                    ?.dailyWeatherData
                    ?.maxTemperature.toString(),
                selectedMinTemperature = data.weatherInfo
                    .completeWeatherData[index]
                    ?.dailyWeatherData
                    ?.minTemperature.toString(),
                selectedWeatherType = data.weatherInfo
                    .completeWeatherData[index]
                    ?.dailyWeatherData
                    ?.weatherType?.iconRes,
                sunriseTime = data.weatherInfo
                    .completeWeatherData[index]
                    ?.dailyWeatherData
                    ?.sunrise?.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM d",
                            Locale.getDefault()
                        )
                    ),
                sunsetTime = data
                    .weatherInfo
                    .completeWeatherData[index]
                    ?.dailyWeatherData
                    ?.sunset?.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM d",
                            Locale.getDefault()
                        )
                    ),
                selectedDayHourlyWeatherList = data.weatherInfo
                    .completeWeatherData[index]
                    ?.hourlyWeatherData,
                todayDate = data.weatherInfo
                    .completeWeatherData[0]
                    ?.dailyWeatherData
                    ?.time?.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM d",
                            Locale.getDefault()
                        )
                    ),
                todayMaxTemperature = data.weatherInfo
                    .completeWeatherData[0]
                    ?.dailyWeatherData?.maxTemperature.toString(),
                todayMinTemperature = data.weatherInfo
                    .completeWeatherData[0]
                    ?.dailyWeatherData?.minTemperature.toString(),
                todayWeatherType = data.weatherInfo
                    .completeWeatherData[0]
                    ?.dailyWeatherData?.weatherType?.iconRes,
                tomorrowDate = data.weatherInfo
                    .completeWeatherData[1]
                    ?.dailyWeatherData
                    ?.time?.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM d",
                            Locale.getDefault()
                        )
                    ),
                tomorrowMaxTemperature = data.weatherInfo
                    .completeWeatherData[1]
                    ?.dailyWeatherData?.maxTemperature.toString(),
                tomorrowMinTemperature = data.weatherInfo
                    .completeWeatherData[1]
                    ?.dailyWeatherData?.minTemperature.toString(),
                tomorrowWeatherType = data.weatherInfo
                    .completeWeatherData[1]
                    ?.dailyWeatherData?.weatherType?.iconRes,
                afterTomorrowDate = data.weatherInfo
                    .completeWeatherData[2]
                    ?.dailyWeatherData
                    ?.time?.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM d",
                            Locale.getDefault()
                        )
                    ),
                afterTomorrowMaxTemperature = data.weatherInfo
                    .completeWeatherData[2]
                    ?.dailyWeatherData?.maxTemperature.toString(),
                afterTomorrowMinTemperature = data.weatherInfo
                    .completeWeatherData[2]
                    ?.dailyWeatherData?.minTemperature.toString(),
                afterTomorrowWeatherType = data.weatherInfo
                    .completeWeatherData[2]
                    ?.dailyWeatherData?.weatherType?.iconRes,
                afterTomorrowDayOfWeek = data.weatherInfo
                    .completeWeatherData[2]
                    ?.dailyWeatherData?.time?.dayOfWeek.toString(),
                inTwoDaysDate = data.weatherInfo
                    .completeWeatherData[3]
                    ?.dailyWeatherData
                    ?.time?.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM d",
                            Locale.getDefault()
                        )
                    ),
                inTwoDaysMaxTemperature = data.weatherInfo
                    .completeWeatherData[3]
                    ?.dailyWeatherData?.maxTemperature.toString(),
                inTwoDaysMinTemperature = data.weatherInfo
                    .completeWeatherData[3]
                    ?.dailyWeatherData?.minTemperature.toString(),
                inTwoDaysWeatherType = data.weatherInfo
                    .completeWeatherData[3]
                    ?.dailyWeatherData?.weatherType?.iconRes,
                inTwoDaysDayOfWeek = data.weatherInfo
                    .completeWeatherData[3]
                    ?.dailyWeatherData?.time?.dayOfWeek.toString(),
                inThreeDaysDate = data.weatherInfo
                    .completeWeatherData[4]
                    ?.dailyWeatherData
                    ?.time?.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM d",
                            Locale.getDefault()
                        )
                    ),
                inThreeDaysMaxTemperature = data.weatherInfo
                    .completeWeatherData[4]
                    ?.dailyWeatherData?.maxTemperature.toString(),
                inThreeDaysMinTemperature = data.weatherInfo
                    .completeWeatherData[4]
                    ?.dailyWeatherData?.minTemperature.toString(),
                inThreeDaysWeatherType = data.weatherInfo
                    .completeWeatherData[4]
                    ?.dailyWeatherData?.weatherType?.iconRes,
                inThreeDaysDayOfWeek = data.weatherInfo
                    .completeWeatherData[4]
                    ?.dailyWeatherData?.time?.dayOfWeek.toString(),
                inFourDaysDate = data.weatherInfo
                    .completeWeatherData[5]
                    ?.dailyWeatherData
                    ?.time?.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM d",
                            Locale.getDefault()
                        )
                    ),
                inFourDaysMaxTemperature = data.weatherInfo
                    .completeWeatherData[5]
                    ?.dailyWeatherData?.maxTemperature.toString(),
                inFourDaysMinTemperature = data.weatherInfo
                    .completeWeatherData[5]
                    ?.dailyWeatherData?.minTemperature.toString(),
                inFourDaysWeatherType = data.weatherInfo
                    .completeWeatherData[5]
                    ?.dailyWeatherData?.weatherType?.iconRes,
                inFourDaysDayOfWeek = data.weatherInfo
                    .completeWeatherData[5]
                    ?.dailyWeatherData?.time?.dayOfWeek.toString(),
                inFiveDaysDate = data.weatherInfo
                    .completeWeatherData[6]
                    ?.dailyWeatherData
                    ?.time?.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM d",
                            Locale.getDefault()
                        )
                    ),
                inFiveDaysMaxTemperature = data.weatherInfo
                    .completeWeatherData[6]
                    ?.dailyWeatherData?.maxTemperature.toString(),
                inFiveDaysMinTemperature = data.weatherInfo
                    .completeWeatherData[6]
                    ?.dailyWeatherData?.minTemperature.toString(),
                inFiveDaysWeatherType = data.weatherInfo
                    .completeWeatherData[6]
                    ?.dailyWeatherData?.weatherType?.iconRes,
                inFiveDaysDayOfWeek = data.weatherInfo
                    .completeWeatherData[6]
                    ?.dailyWeatherData?.time?.dayOfWeek.toString()
            )
        }
    }


    private fun processSuccessState(data: FavoritePlaceInfo, index: Int) {
        if (data.weatherInfo == null) {
            throw IllegalArgumentException("Weather info cannot be null in loading or success state")
        }
        _state.update { state ->
            state.copy(
                isLoading = false,
                toolbarTitle = data.placeName,
                selectedDate = data.weatherInfo
                    .completeWeatherData[index]
                    ?.dailyWeatherData
                    ?.time?.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM d",
                            Locale.getDefault()
                        )
                    ),
                selectedMaxTemperature = data.weatherInfo
                    .completeWeatherData[index]
                    ?.dailyWeatherData
                    ?.maxTemperature.toString(),
                selectedMinTemperature = data.weatherInfo
                    .completeWeatherData[index]
                    ?.dailyWeatherData
                    ?.minTemperature.toString(),
                selectedWeatherType = data.weatherInfo
                    .completeWeatherData[index]
                    ?.dailyWeatherData
                    ?.weatherType?.iconRes,
                sunriseTime = data.weatherInfo
                    .completeWeatherData[index]
                    ?.dailyWeatherData
                    ?.sunrise?.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM d",
                            Locale.getDefault()
                        )
                    ),
                sunsetTime = data
                    .weatherInfo
                    .completeWeatherData[index]
                    ?.dailyWeatherData
                    ?.sunset?.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM d",
                            Locale.getDefault()
                        )
                    ),
                selectedDayHourlyWeatherList = data.weatherInfo
                    .completeWeatherData[index]
                    ?.hourlyWeatherData,
                todayDate = data.weatherInfo
                    .completeWeatherData[0]
                    ?.dailyWeatherData
                    ?.time?.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM d",
                            Locale.getDefault()
                        )
                    ),
                todayMaxTemperature = data.weatherInfo
                    .completeWeatherData[0]
                    ?.dailyWeatherData?.maxTemperature.toString(),
                todayMinTemperature = data.weatherInfo
                    .completeWeatherData[0]
                    ?.dailyWeatherData?.minTemperature.toString(),
                todayWeatherType = data.weatherInfo
                    .completeWeatherData[0]
                    ?.dailyWeatherData?.weatherType?.iconRes,
                tomorrowDate = data.weatherInfo
                    .completeWeatherData[1]
                    ?.dailyWeatherData
                    ?.time?.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM d",
                            Locale.getDefault()
                        )
                    ),
                tomorrowMaxTemperature = data.weatherInfo
                    .completeWeatherData[1]
                    ?.dailyWeatherData?.maxTemperature.toString(),
                tomorrowMinTemperature = data.weatherInfo
                    .completeWeatherData[1]
                    ?.dailyWeatherData?.minTemperature.toString(),
                tomorrowWeatherType = data.weatherInfo
                    .completeWeatherData[1]
                    ?.dailyWeatherData?.weatherType?.iconRes,
                afterTomorrowDate = data.weatherInfo
                    .completeWeatherData[2]
                    ?.dailyWeatherData
                    ?.time?.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM d",
                            Locale.getDefault()
                        )
                    ),
                afterTomorrowMaxTemperature = data.weatherInfo
                    .completeWeatherData[2]
                    ?.dailyWeatherData?.maxTemperature.toString(),
                afterTomorrowMinTemperature = data.weatherInfo
                    .completeWeatherData[2]
                    ?.dailyWeatherData?.minTemperature.toString(),
                afterTomorrowWeatherType = data.weatherInfo
                    .completeWeatherData[2]
                    ?.dailyWeatherData?.weatherType?.iconRes,
                afterTomorrowDayOfWeek = data.weatherInfo
                    .completeWeatherData[2]
                    ?.dailyWeatherData?.time?.dayOfWeek.toString(),
                inTwoDaysDate = data.weatherInfo
                    .completeWeatherData[3]
                    ?.dailyWeatherData
                    ?.time?.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM d",
                            Locale.getDefault()
                        )
                    ),
                inTwoDaysMaxTemperature = data.weatherInfo
                    .completeWeatherData[3]
                    ?.dailyWeatherData?.maxTemperature.toString(),
                inTwoDaysMinTemperature = data.weatherInfo
                    .completeWeatherData[3]
                    ?.dailyWeatherData?.minTemperature.toString(),
                inTwoDaysWeatherType = data.weatherInfo
                    .completeWeatherData[3]
                    ?.dailyWeatherData?.weatherType?.iconRes,
                inTwoDaysDayOfWeek = data.weatherInfo
                    .completeWeatherData[3]
                    ?.dailyWeatherData?.time?.dayOfWeek.toString(),
                inThreeDaysDate = data.weatherInfo
                    .completeWeatherData[4]
                    ?.dailyWeatherData
                    ?.time?.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM d",
                            Locale.getDefault()
                        )
                    ),
                inThreeDaysMaxTemperature = data.weatherInfo
                    .completeWeatherData[4]
                    ?.dailyWeatherData?.maxTemperature.toString(),
                inThreeDaysMinTemperature = data.weatherInfo
                    .completeWeatherData[4]
                    ?.dailyWeatherData?.minTemperature.toString(),
                inThreeDaysWeatherType = data.weatherInfo
                    .completeWeatherData[4]
                    ?.dailyWeatherData?.weatherType?.iconRes,
                inThreeDaysDayOfWeek = data.weatherInfo
                    .completeWeatherData[4]
                    ?.dailyWeatherData?.time?.dayOfWeek.toString(),
                inFourDaysDate = data.weatherInfo
                    .completeWeatherData[5]
                    ?.dailyWeatherData
                    ?.time?.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM d",
                            Locale.getDefault()
                        )
                    ),
                inFourDaysMaxTemperature = data.weatherInfo
                    .completeWeatherData[5]
                    ?.dailyWeatherData?.maxTemperature.toString(),
                inFourDaysMinTemperature = data.weatherInfo
                    .completeWeatherData[5]
                    ?.dailyWeatherData?.minTemperature.toString(),
                inFourDaysWeatherType = data.weatherInfo
                    .completeWeatherData[5]
                    ?.dailyWeatherData?.weatherType?.iconRes,
                inFourDaysDayOfWeek = data.weatherInfo
                    .completeWeatherData[5]
                    ?.dailyWeatherData?.time?.dayOfWeek.toString(),
                inFiveDaysDate = data.weatherInfo
                    .completeWeatherData[6]
                    ?.dailyWeatherData
                    ?.time?.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM d",
                            Locale.getDefault()
                        )
                    ),
                inFiveDaysMaxTemperature = data.weatherInfo
                    .completeWeatherData[6]
                    ?.dailyWeatherData?.maxTemperature.toString(),
                inFiveDaysMinTemperature = data.weatherInfo
                    .completeWeatherData[6]
                    ?.dailyWeatherData?.minTemperature.toString(),
                inFiveDaysWeatherType = data.weatherInfo
                    .completeWeatherData[6]
                    ?.dailyWeatherData?.weatherType?.iconRes,
                inFiveDaysDayOfWeek = data.weatherInfo
                    .completeWeatherData[6]
                    ?.dailyWeatherData?.time?.dayOfWeek.toString()
            )
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
