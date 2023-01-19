package com.yotfr.weather.presentation.searchplaces

import com.yotfr.weather.domain.model.PlaceInfo

sealed interface SearchPlacesEvent {
    data class SearchQueryChanged(val newSearchQuery: String) : SearchPlacesEvent
    data class AddPlaceToFavorite(val place: PlaceInfo) : SearchPlacesEvent
}
