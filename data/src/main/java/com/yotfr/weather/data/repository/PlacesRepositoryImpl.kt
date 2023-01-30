package com.yotfr.weather.data.repository

import android.util.Log
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
import retrofit2.HttpException
import java.io.IOException
import java.util.Locale
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
                    searchQuery = searchQuery,
                    language = Locale.getDefault().language
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
                                        message = "Incorrect url"
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

    override suspend fun updateFavoritePlaceInfo(placeId: Long): Response<List<FavoritePlaceInfo>>? {
        try {
            val fetchedPlace = placesApi.getPlaceById(
                id = placeId,
                language = Locale.getDefault().language
            )
            placesDao.insertFavoritePlace(
                favoritePlaceEntity = fetchedPlace.mapToFavoritePlaceEntity()
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

    override suspend fun getFavoritePlaces(isUpdated: Boolean): Response<List<FavoritePlaceInfo>> {
        try {
            val placesWithWeatherCache = placesDao.getAllFavoritePlaces()

            val mappedFavoritePlaces = placesWithWeatherCache.map { placeWithWeatherCache ->
                placeWithWeatherCache.mapToFavoritePlaceInfo(
                    timeZone = placeWithWeatherCache.favoritePlaceEntity.timeZone
                )
            }

            if (!isUpdated) {
                return Response.Loading(
                    data = mappedFavoritePlaces
                )
            }
            return Response.Success(
                data = mappedFavoritePlaces
            )
        } catch (e: Exception) {
            Log.d("TEST", "exceptionnn $e")
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

    override suspend fun addFavoritePlace(place: PlaceInfo): Long {
        return placesDao.insertFavoritePlace(
            favoritePlaceEntity = place.mapToFavoritePlaceEntity()
        )
    }

    // Param [isCacheUpdated] determines whether the cache was updated at the time the function was called
    override suspend fun getFavoritePlace(
        placeId: Long,
        isCacheUpdated: Boolean
    ): Response<FavoritePlaceInfo> {
        try {
            val placeEntity = placesDao.getFavoritePlaceById(placeId)
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

    // Fetch and update information about the current user's location
    override suspend fun updateCurrentPlaceInfo(
        latitude: Double,
        longitude: Double
    ): Response<FavoritePlaceInfo>? {
        try {
            // Get information about the current user's location with reverse geocoding API
            val fetchedPlace = getPlaceNameApi.getPlaceNameByCoordinates(
                latitude = latitude,
                longitude = longitude,
                language = Locale.getDefault().language
            )

            // Room insert onConflictStrategy = OnConflictStrategy.REPLACE
            placesDao.insertFavoritePlace(
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
