package com.yotfr.weather.presentation.places.citymanagement

sealed interface CityManagementEvent {
    data class SearchQueryChanged(val newSearchQuery: String) : CityManagementEvent
}