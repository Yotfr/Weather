package com.yotfr.weather.domain.util

sealed interface Cause {
    object BadConnectionException : Cause
    object GpsDisabledException : Cause
    object PermissionsPermanentlyDisabledException : Cause
    data class UnknownException(val message: String? = null) : Cause
}
