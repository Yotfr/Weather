package com.yotfr.weather.presentation.searchplaces

import com.yotfr.weather.domain.model.PlaceInfo

data class SearchPlacesState(
    val isLoading: Boolean = false,
    val rvList: List<PlaceInfo> = emptyList()
) 
