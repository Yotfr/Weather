package com.yotfr.weather.presentation.favoriteplaces

import com.yotfr.weather.domain.model.FavoritePlaceInfo

data class FavoritePlacesState(
    val isLoading: Boolean = false,
    val rvList: List<FavoritePlaceInfo> = emptyList()
)