package com.yotfr.weather.presentation.utils

import java.io.Serializable

data class LocationInfo(
    val latitude: Double ? = null,
    val longitude: Double ? = null,
    val timeZone: String ? = null
) : Serializable
