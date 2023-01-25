package com.yotfr.weather.domain.model

/**
 * [PlaceInfo] contains information about a searched place
 */
data class PlaceInfo(
    val id: Long,
    val placeName: String,
    val latitude: Double,
    val longitude: Double,
    val countryName: String,
    val timeZone: String
) : java.io.Serializable
