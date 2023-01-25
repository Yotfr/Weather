package com.yotfr.weather.domain.model

/**
 * [FavoritePlaceInfo] contains information about a favorite place
 */
data class FavoritePlaceInfo(
    val id: Long,
    val placeName: String,
    val latitude: Double,
    val longitude: Double,
    val countryName: String,
    val timeZone: String,
    val weatherInfo: WeatherInfo?
)