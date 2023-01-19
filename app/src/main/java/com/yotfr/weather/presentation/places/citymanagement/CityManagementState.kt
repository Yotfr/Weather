package com.yotfr.weather.presentation.places.citymanagement

import com.yotfr.weather.domain.places.model.LocationInfo

data class CityManagementState(
    val isLoading: Boolean = false,
    val rvList: List<LocationInfo> = emptyList()
) 
