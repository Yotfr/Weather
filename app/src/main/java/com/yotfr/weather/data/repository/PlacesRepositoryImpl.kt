package com.yotfr.weather.data.repository

import com.yotfr.weather.data.datasource.local.PlacesDao
import com.yotfr.weather.data.datasource.remote.PlacesApi
import com.yotfr.weather.data.util.mapToFavoritePlaceEntity
import com.yotfr.weather.data.util.mapToFavoritePlaceInfo
import com.yotfr.weather.data.util.mapToPlaceInfo
import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.model.PlaceInfo
import com.yotfr.weather.domain.repository.PlacesRepository
import com.yotfr.weather.domain.util.Cause
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val placesApi: PlacesApi,
    private val placesDao: PlacesDao
) : PlacesRepository {

    override suspend fun getPlacesThatMatchesQuery(searchQuery: String): Flow<Response<List<PlaceInfo>>> =
        flow {
            try {
                emit(Response.Loading<List<PlaceInfo>>())
                val queryResult = placesApi.getPlacesWithCoordinates(
                    searchQuery = searchQuery
                )
                val mappedQueryResult = queryResult.placeData.map { locationData ->
                    locationData.mapToPlaceInfo()
                }
                emit(Response.Success(mappedQueryResult))
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            400 -> emit(
                                Response.Exception(
                                    cause = Cause.UnknownException(e.message)
                                )
                            )
                            else -> emit(
                                Response.Exception(
                                    cause = Cause.UnknownException(e.message)
                                )
                            )
                        }
                    }
                    is IOException -> {
                        emit(
                            Response.Exception(
                                cause = Cause.BadConnectionException
                            )
                        )
                    }
                    else -> {
                        emit(
                            Response.Exception(
                                cause = Cause.UnknownException(e.message)
                            )
                        )
                    }
                }
            }
        }

    override suspend fun getFavoritePlaces(): Flow<List<FavoritePlaceInfo>> {
        return placesDao.getAllFavoritePlaces().map { placesList ->
            placesList.map { place ->
                place.mapToFavoritePlaceInfo()
            }
        }
    }

    override suspend fun addFavoritePlace(place: PlaceInfo): Long {
        return placesDao.addFavoritePlace(
            favoritePlaceEntity = place.mapToFavoritePlaceEntity()
        )
    }

    override suspend fun getFavoritePlaceByPlaceId(placeId: Long): Flow<FavoritePlaceInfo> {
        return placesDao.getFavoritePlaceByPlaceId(placeId).map {
            it.mapToFavoritePlaceInfo()
        }
    }
}
