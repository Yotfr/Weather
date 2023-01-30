package com.yotfr.weather.presentation.searchplaces

sealed interface SearchPlacesEvent {
    data class SearchQueryChanged(val newSearchQuery: String) : SearchPlacesEvent
}
