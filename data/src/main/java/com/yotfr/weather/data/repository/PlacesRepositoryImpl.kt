package com.yotfr.weather.data.repository

import com.yotfr.weather.data.datasource.local.PlacesDao
import com.yotfr.weather.data.datasource.local.entities.FavoritePlaceEntity
import com.yotfr.weather.data.datasource.remote.GetPlaceNameApi
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
import java.util.TimeZone
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val placesApi: PlacesApi,
    private val placesDao: PlacesDao,
    private val getPlaceNameApi: GetPlaceNameApi
) : PlacesRepository {

    override suspend fun getPlacesThatMatchesQuery(searchQuery: String): Flow<Response<List<PlaceInfo>>> =
        flow {
            try {
                emit(Response.Loading())
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
                                    cause = Cause.UnknownException(
                                        message = e.message
                                    )
                                )
                            )
                            else -> emit(
                                Response.Exception(
                                    cause = Cause.UnknownException(
                                        message = e.message
                                    )
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
                                cause = Cause.UnknownException(
                                    message = e.message
                                )
                            )
                        )
                    }
                }
            }
        }

    override suspend fun getFavoritePlaces(): Flow<List<FavoritePlaceInfo>> {
        return placesDao.getAllFavoritePlaces().map {
            it.map {
                it.mapToFavoritePlaceInfo(
                    timeZone = it.favoritePlaceEntity.timeZone
                )
            }
        }
    }

    override suspend fun addFavoritePlace(place: PlaceInfo): Long {
        return placesDao.addFavoritePlace(
            favoritePlaceEntity = place.mapToFavoritePlaceEntity()
        )
    }

    override suspend fun getFavoritePlace(
        placeId: Long,
        isCacheUpdated: Boolean
    ): Response<FavoritePlaceInfo> {
        try {
            val placeEntity = placesDao.getFavoritePlaceByPlaceId(placeId)
            val mappedPlaceEntity = placeEntity.mapToFavoritePlaceInfo(
                timeZone = placeEntity.favoritePlaceEntity.timeZone
            )
            if (!isCacheUpdated) {
                return Response.Loading(
                    data = mappedPlaceEntity
                )
            }
            return Response.Success(
                data = mappedPlaceEntity
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.Exception(
                cause = Cause.UnknownException()
            )
        }
    }

    override suspend fun deleteFavoritePlace(place: FavoritePlaceInfo) {
        placesDao.deleteFavoritePlace(place.mapToFavoritePlaceEntity())
    }

    override suspend fun updateCurrentPlaceInfo(latitude: Double, longitude: Double): Response<FavoritePlaceInfo>? {
        try {
            val fetchedPlace = getPlaceNameApi.getPlaceNameByCoordinates(
                latitude = latitude,
                longitude = longitude
            )

            placesDao.addFavoritePlace(
                favoritePlaceEntity = FavoritePlaceEntity(
                    id = -2L,
                    placeName = fetchedPlace.city,
                    latitude = latitude,
                    longitude = longitude,
                    countryName = fetchedPlace.countryName,
                    timeZone = TimeZone.getDefault().id
                )
            )
            return null
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    return Response.Exception(
                        cause = Cause.UnknownException()
                    )
                }
                is IOException -> {
                    return Response.Exception(
                        cause = Cause.BadConnectionException
                    )
                }
                else -> {
                    return Response.Exception(
                        cause = Cause.UnknownException()
                    )
                }
            }
        }
    }
}
