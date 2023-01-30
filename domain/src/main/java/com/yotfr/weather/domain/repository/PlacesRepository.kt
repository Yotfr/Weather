package com.yotfr.weather.domain.repository

import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.model.PlaceInfo
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {

    suspend fun getPlacesThatMatchesQuery(searchQuery: String): Flow<Response<List<PlaceInfo>>>

    suspend fun getFavoritePlaces(isUpdated: Boolean): Response<List<FavoritePlaceInfo>>

    suspend fun addFavoritePlace(place: PlaceInfo): Long

    // Fetch and update information about the current user's location
    suspend fun updateCurrentPlaceInfo(latitude: Double, longitude: Double): Response<FavoritePlaceInfo>?

    // Param [isCacheUpdated] determines whether the cache was updated at the time the function was called
    suspend fun getFavoritePlace(placeId: Long, isCacheUpdated: Boolean): Response<FavoritePlaceInfo>

    suspend fun deleteFavoritePlace(place: FavoritePlaceInfo)

    suspend fun updateFavoritePlaceInfo(placeId: Long): Response<List<FavoritePlaceInfo>>?
}
