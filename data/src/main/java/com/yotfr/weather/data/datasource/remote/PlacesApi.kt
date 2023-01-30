package com.yotfr.weather.data.datasource.remote

import com.yotfr.weather.data.datasource.remote.dto.PlaceDataDto
import com.yotfr.weather.data.datasource.remote.dto.PlacesDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {
    @GET("v1/search")
    suspend fun getPlacesWithCoordinates(
        @Query("name") searchQuery: String,
        @Query("language") language: String
    ): PlacesDto

    @GET("v1/get")
    suspend fun getPlaceById(
        @Query("id") id: Long,
        @Query("language") language: String
    ): PlaceDataDto
}