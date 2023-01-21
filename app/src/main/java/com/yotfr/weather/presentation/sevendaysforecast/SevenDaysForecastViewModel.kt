package com.yotfr.weather.presentation.sevendaysforecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.weather.domain.usecases.LoadWeatherInfoUseCase
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class SevenDaysForecastViewModel @Inject constructor(
    private val getWeatherInfoUseCase: LoadWeatherInfoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SevenDaysForecastState())
    val state = _state.asStateFlow()

    private val _selectedIndex = MutableStateFlow(0)
    val selectedIndex = _selectedIndex.asStateFlow()

    init {
        getWeather()
    }

    private fun getWeather() {
        viewModelScope.launch {
            getWeatherInfoUseCase(
                latitude = locationInfo.value.latitude,
                longitude = locationInfo.value.longitude,
                timezone = locationInfo.value.timeZone
            ).combine(
                selectedIndex
            ) { response, index ->
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
                                selectedDate = response.data
                                    .completeWeatherData[index]
                                    ?.dailyWeatherData
                                    ?.time?.format(
                                        DateTimeFormatter.ofPattern(
                                            "MMMM d",
                                            Locale.getDefault()
                                        )
                                    ),
                                selectedMaxTemperature = response.data
                                    .completeWeatherData[index]
                                    ?.dailyWeatherData
                                    ?.maxTemperature.toString(),
                                selectedMinTemperature = response.data
                                    .completeWeatherData[index]
                                    ?.dailyWeatherData
                                    ?.minTemperature.toString(),
                                selectedWeatherType = response.data
                                    .completeWeatherData[index]
                                    ?.dailyWeatherData
                                    ?.weatherType?.iconRes,
                                sunriseTime = response.data
                                    .completeWeatherData[index]
                                    ?.dailyWeatherData
                                    ?.sunrise?.format(
                                        DateTimeFormatter.ofPattern(
                                            "MMMM d",
                                            Locale.getDefault()
                                        )
                                    ),
                                sunsetTime = response.data
                                    .completeWeatherData[index]
                                    ?.dailyWeatherData
                                    ?.sunset?.format(
                                        DateTimeFormatter.ofPattern(
                                            "MMMM d",
                                            Locale.getDefault()
                                        )
                                    ),
                                selectedDayHourlyWeatherList = response.data
                                    .completeWeatherData[index]
                                    ?.hourlyWeatherData,
                                todayDate = response.data
                                    .completeWeatherData[0]
                                    ?.dailyWeatherData
                                    ?.time?.format(
                                        DateTimeFormatter.ofPattern(
                                            "MMMM d",
                                            Locale.getDefault()
                                        )
                                    ),
                                todayMaxTemperature = response.data
                                    .completeWeatherData[0]
                                    ?.dailyWeatherData?.maxTemperature.toString(),
                                todayMinTemperature = response.data
                                    .completeWeatherData[0]
                                    ?.dailyWeatherData?.minTemperature.toString(),
                                todayWeatherType = response.data
                                    .completeWeatherData[0]
                                    ?.dailyWeatherData?.weatherType?.iconRes,
                                tomorrowDate = response.data
                                    .completeWeatherData[1]
                                    ?.dailyWeatherData
                                    ?.time?.format(
                                        DateTimeFormatter.ofPattern(
                                            "MMMM d",
                                            Locale.getDefault()
                                        )
                                    ),
                                tomorrowMaxTemperature = response.data
                                    .completeWeatherData[1]
                                    ?.dailyWeatherData?.maxTemperature.toString(),
                                tomorrowMinTemperature = response.data
                                    .completeWeatherData[1]
                                    ?.dailyWeatherData?.minTemperature.toString(),
                                tomorrowWeatherType = response.data
                                    .completeWeatherData[1]
                                    ?.dailyWeatherData?.weatherType?.iconRes,
                                afterTomorrowDate = response.data
                                    .completeWeatherData[2]
                                    ?.dailyWeatherData
                                    ?.time?.format(
                                        DateTimeFormatter.ofPattern(
                                            "MMMM d",
                                            Locale.getDefault()
                                        )
                                    ),
                                afterTomorrowMaxTemperature = response.data
                                    .completeWeatherData[2]
                                    ?.dailyWeatherData?.maxTemperature.toString(),
                                afterTomorrowMinTemperature = response.data
                                    .completeWeatherData[2]
                                    ?.dailyWeatherData?.minTemperature.toString(),
                                afterTomorrowWeatherType = response.data
                                    .completeWeatherData[2]
                                    ?.dailyWeatherData?.weatherType?.iconRes,
                                afterTomorrowDayOfWeek = response.data
                                    .completeWeatherData[2]
                                    ?.dailyWeatherData?.time?.dayOfWeek.toString(),
                                inTwoDaysDate = response.data
                                    .completeWeatherData[3]
                                    ?.dailyWeatherData
                                    ?.time?.format(
                                        DateTimeFormatter.ofPattern(
                                            "MMMM d",
                                            Locale.getDefault()
                                        )
                                    ),
                                inTwoDaysMaxTemperature = response.data
                                    .completeWeatherData[3]
                                    ?.dailyWeatherData?.maxTemperature.toString(),
                                inTwoDaysMinTemperature = response.data
                                    .completeWeatherData[3]
                                    ?.dailyWeatherData?.minTemperature.toString(),
                                inTwoDaysWeatherType = response.data
                                    .completeWeatherData[3]
                                    ?.dailyWeatherData?.weatherType?.iconRes,
                                inTwoDaysDayOfWeek = response.data
                                    .completeWeatherData[3]
                                    ?.dailyWeatherData?.time?.dayOfWeek.toString(),
                                inThreeDaysDate = response.data
                                    .completeWeatherData[4]
                                    ?.dailyWeatherData
                                    ?.time?.format(
                                        DateTimeFormatter.ofPattern(
                                            "MMMM d",
                                            Locale.getDefault()
                                        )
                                    ),
                                inThreeDaysMaxTemperature = response.data
                                    .completeWeatherData[4]
                                    ?.dailyWeatherData?.maxTemperature.toString(),
                                inThreeDaysMinTemperature = response.data
                                    .completeWeatherData[4]
                                    ?.dailyWeatherData?.minTemperature.toString(),
                                inThreeDaysWeatherType = response.data
                                    .completeWeatherData[4]
                                    ?.dailyWeatherData?.weatherType?.iconRes,
                                inThreeDaysDayOfWeek = response.data
                                    .completeWeatherData[4]
                                    ?.dailyWeatherData?.time?.dayOfWeek.toString(),
                                inFourDaysDate = response.data
                                    .completeWeatherData[5]
                                    ?.dailyWeatherData
                                    ?.time?.format(
                                        DateTimeFormatter.ofPattern(
                                            "MMMM d",
                                            Locale.getDefault()
                                        )
                                    ),
                                inFourDaysMaxTemperature = response.data
                                    .completeWeatherData[5]
                                    ?.dailyWeatherData?.maxTemperature.toString(),
                                inFourDaysMinTemperature = response.data
                                    .completeWeatherData[5]
                                    ?.dailyWeatherData?.minTemperature.toString(),
                                inFourDaysWeatherType = response.data
                                    .completeWeatherData[5]
                                    ?.dailyWeatherData?.weatherType?.iconRes,
                                inFourDaysDayOfWeek = response.data
                                    .completeWeatherData[5]
                                    ?.dailyWeatherData?.time?.dayOfWeek.toString(),
                                inFiveDaysDate = response.data
                                    .completeWeatherData[6]
                                    ?.dailyWeatherData
                                    ?.time?.format(
                                        DateTimeFormatter.ofPattern(
                                            "MMMM d",
                                            Locale.getDefault()
                                        )
                                    ),
                                inFiveDaysMaxTemperature = response.data
                                    .completeWeatherData[6]
                                    ?.dailyWeatherData?.maxTemperature.toString(),
                                inFiveDaysMinTemperature = response.data
                                    .completeWeatherData[6]
                                    ?.dailyWeatherData?.minTemperature.toString(),
                                inFiveDaysWeatherType = response.data
                                    .completeWeatherData[6]
                                    ?.dailyWeatherData?.weatherType?.iconRes,
                                inFiveDaysDayOfWeek = response.data
                                    .completeWeatherData[6]
                                    ?.dailyWeatherData?.time?.dayOfWeek.toString()
                            )
                        }
                    }
                    is Response.Exception -> {}
                }
            }.collect()
        }
    }

    fun onEvent(event: SevenDaysForecastEvent) {
        when (event) {
            is SevenDaysForecastEvent.SelectedDayIndexChanged -> {
                _selectedIndex.value = event.newIndex
            }
        }
    }
}
