package com.yotfr.weather.presentation.favoriteplaces

import com.yotfr.weather.domain.model.PlaceInfo

data class FavoritePlacesState(
    val isLoading: Boolean = false,
    val rvList: List<PlaceInfo> = emptyList()
)