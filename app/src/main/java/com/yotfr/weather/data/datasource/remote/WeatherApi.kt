package com.yotfr.weather.data.datasource.remote

import com.yotfr.weather.data.datasource.remote.dto.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET(
        "v1/forecast?hourly=temperature_2m,weathercode,relativehumidity_2m,apparent_temperature,windspeed_10m," +
            "pressure_msl&daily=weathercode,temperature_2m_max,temperature_2m_min,sunrise,sunset"
    )
    suspend fun getWeatherData(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("timezone") timezone: String
    ): WeatherDto
}
