package com.yotfr.weather.presentation.favoriteplaces

import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.model.TemperatureUnits

data class FavoritePlacesState(
    val isLoading: Boolean = false,
    val rvList: List<FavoritePlaceInfo> = emptyList(),
    val temperatureUnit: TemperatureUnits = TemperatureUnits.CELSIUS
)