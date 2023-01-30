package com.yotfr.weather.domain.repository

import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.model.PlaceInfo
import com.yotfr.weather.domain.util.Response

interface WeatherRepository {

    /*
     Fetch information about the weather in place with selected place id and save it to the database
     */
    suspend fun createWeatherCacheForPlace(
        placeId: Long,
        latitude: Double,
        longitude: Double,
        timeZone: String,
        temperatureUnits: String,
        windSpeedUnits: String
    )

    /*
     Fetch information about the weather in place with selected place id and update database data
     */
    suspend fun updateWeatherCacheRelatedToPlaceId(
        placeId: Long,
        latitude: Double,
        longitude: Double,
        timeZone: String,
        temperatureUnits: String,
        windSpeedUnits: String
    ): Response<FavoritePlaceInfo>?

    /*
    Delete weather cache related to selected placeId
     */
    suspend fun deleteWeatherDataRelatedToPlaceId(
        placeId: Long
    )

    /*
    Fetch and return information about the weather in selected place
     */
    suspend fun getWeatherDataForSearchedPlace(
        placeInfo: PlaceInfo,
        temperatureUnits: String,
        windSpeedUnits: String
    ): Response<FavoritePlaceInfo>

}
