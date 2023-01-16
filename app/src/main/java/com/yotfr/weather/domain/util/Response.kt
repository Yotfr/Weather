package com.yotfr.weather.domain.util

sealed class Response<out S> {
    data class Loading<S>(val data: S? = null) : Response<Nothing>()
    data class Success<S>(val data: S) : Response<S>()
    data class Exception(val cause: Cause) : Response<Nothing>()
}
