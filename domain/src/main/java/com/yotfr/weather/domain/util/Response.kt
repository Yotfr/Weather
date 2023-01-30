package com.yotfr.weather.domain.util

sealed class Response<T>(val data: T? = null, val cause: Cause? = null) {
    class Loading<T>(data: T? = null) : Response<T>(data, null)
    class Success<T>(data: T?) : Response<T>(data, null)
    class Exception<T>(cause: Cause) : Response<T>(null, cause)
}

