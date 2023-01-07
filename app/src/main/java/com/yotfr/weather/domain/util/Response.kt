package com.yotfr.weather.domain.util

sealed class Response<T>(val data: T? = null, val message: String? = null) {
    class Loading<T> : Response<T>()
    class Succecss<T>(data: T?) : Response<T>(data)
    class Error<T> (message: String?, data: T? = null) : Response<T>(data, message)
}
