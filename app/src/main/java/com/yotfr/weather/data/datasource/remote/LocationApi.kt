package com.yotfr.weather.data.datasource.remote

import com.yotfr.weather.data.datasource.remote.dto.LocationDto
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {
    @GET("v1/search")
    suspend fun getPlacesWithCoordinates(
        @Query("name") searchQuery: String,
        @Query("count") count: Int = 10
    ): LocationDto
}