package com.yotfr.weather.data.datasource.remote

import com.yotfr.weather.data.datasource.remote.dto.PlaceName
import retrofit2.http.GET
import retrofit2.http.Query

interface GetPlaceNameApi {

    @GET("data/reverse-geocode-client")
    suspend fun getPlaceNameByCoordinates(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("localityLanguage") language: String
    ): PlaceName
}