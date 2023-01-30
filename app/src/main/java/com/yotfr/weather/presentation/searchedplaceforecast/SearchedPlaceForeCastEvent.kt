package com.yotfr.weather.presentation.searchedplaceforecast

import com.yotfr.weather.domain.model.PlaceInfo

sealed interface SearchedPlaceForeCastEvent {
    data class SelectedDayIndexChanged(val newIndex: Int) : SearchedPlaceForeCastEvent
    data class ChangePlaceInfo(val place: PlaceInfo) : SearchedPlaceForeCastEvent
    object StarPressed : SearchedPlaceForeCastEvent
}
