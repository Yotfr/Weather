package com.yotfr.weather.domain.util

sealed interface Cause {
    object BadConnectionException : Cause
    data class UnknownException(val message: String? = null) : Cause
}