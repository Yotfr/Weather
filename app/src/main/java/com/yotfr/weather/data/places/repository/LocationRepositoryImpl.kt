package com.yotfr.weather.data.places.repository

import com.yotfr.weather.data.places.datasource.remote.LocationApi
import com.yotfr.weather.data.places.util.mapToLocationInfo
import com.yotfr.weather.domain.places.model.LocationInfo
import com.yotfr.weather.domain.places.repository.LocationRepository
import com.yotfr.weather.domain.util.Cause
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationApi: LocationApi
) : LocationRepository {

    override suspend fun getLocation(searchQuery: String): Flow<Response<List<LocationInfo>>> =
        flow {
            try {
                emit(Response.Loading<List<LocationInfo>>())
                val queryResult = locationApi.getPlacesWithCoordinates(
                    searchQuery = searchQuery
                )
                val mappedQueryResult = queryResult.weatherData.map { locationData ->
                    locationData.mapToLocationInfo()
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
}
