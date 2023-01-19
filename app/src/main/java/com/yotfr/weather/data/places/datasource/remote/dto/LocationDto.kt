package com.yotfr.weather.data.places.datasource.remote.dto

import com.squareup.moshi.Json

data class LocationDto(
    @field:Json(name = "results")
    val weatherData: List<LocationDataDto>
)