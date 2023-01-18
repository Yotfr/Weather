package com.yotfr.weather.domain.model

data class LocationInfo(
    val id: Long,
    val placeName: String,
    val latitude: Double,
    val longitude: Double,
    val countryName: String,
    val timeZone: String
)
