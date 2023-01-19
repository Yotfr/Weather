package com.yotfr.weather.data.places.datasource.remote.dto

import com.squareup.moshi.Json

data class LocationDataDto(
    val id: Long,
    @field:Json(name = "name")
    val placeName: String,
    val latitude: Double,
    val longitude: Double,
    @field:Json(name = "country")
    val countryName: String,
    @field:Json(name = "timezone")
    val timeZone: String
)