package com.yotfr.weather.presentation.sevendaysforecast

sealed interface SevenDaysForecastEvent {
    data class SelectedDayIndexChanged(val newIndex: Int) : SevenDaysForecastEvent
    data class ChangeCurrentSelectedPlaceId(val newPlaceId: Long) : SevenDaysForecastEvent
    object Swiped : SevenDaysForecastEvent
}
