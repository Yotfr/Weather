package com.yotfr.weather.domain.repository

import com.yotfr.weather.domain.model.PlaceInfo
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {
    suspend fun getPlacesThatMatchesQuery(searchQuery: String): Flow<Response<List<PlaceInfo>>>

    suspend fun getFavoritePlaces(): Flow<List<PlaceInfo>>

    suspend fun addFavoritePlace(place: PlaceInfo)
}