package com.yotfr.weather.data.datasource.remote.dto

import com.squareup.moshi.Json

data class PlacesDto(
    @field:Json(name = "results")
    val placeData: List<PlaceDataDto>
)