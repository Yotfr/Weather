package com.yotfr.weather.domain.util

sealed class Response<out S> {
    object Loading : Response<Nothing>()
    data class Success<S>(val data: S) : Response<S>()
    class Exception(val cause: Cause) : Response<Nothing>()
}
