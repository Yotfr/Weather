package com.yotfr.weather.domain.repository

import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.model.PlaceInfo
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {

    suspend fun getPlacesThatMatchesQuery(searchQuery: String): Flow<Response<List<PlaceInfo>>>

    suspend fun getFavoritePlaces(): Flow<List<FavoritePlaceInfo>>

    suspend fun addFavoritePlace(place: PlaceInfo): Long

    suspend fun getFavoritePlaceByPlaceId(placeId: Long): Flow<FavoritePlaceInfo>
}
