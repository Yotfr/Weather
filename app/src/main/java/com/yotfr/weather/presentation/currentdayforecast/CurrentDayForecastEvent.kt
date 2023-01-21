package com.yotfr.weather.presentation.currentdayforecast

sealed interface CurrentDayForecastEvent {
    data class ChangeCurrentSelectedPlaceId(val newPlaceId: Long) : CurrentDayForecastEvent
}
