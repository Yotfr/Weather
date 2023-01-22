package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.location.LocationTracker
import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.repository.PlacesRepository
import com.yotfr.weather.domain.repository.WeatherRepository
import com.yotfr.weather.domain.util.Cause
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import java.util.TimeZone

class GetWeatherInfoForFavoritePlace(
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker,
    private val placesRepository: PlacesRepository
) {
    suspend operator fun invoke(favoritePlaceId: Long?): Flow<Response<FavoritePlaceInfo>> = flow {
        // emit loading state
        emit(Response.Loading())

        if (favoritePlaceId != null && favoritePlaceId != -2L) {
            // In case place is not current place by geolocation
            try {
                // Get favorite place by id (favoritePlaceInfo) contains cached weather data
                val favoritePlaceInfo = placesRepository.getFavoritePlaceByPlaceId(
                    placeId = favoritePlaceId
                )
                if (favoritePlaceInfo.weatherInfo != null) {
                    // emit loading state with old cached data
                    emit(
                        Response.Loading(
                            data = favoritePlaceInfo
                        )
                    )
                }

                // update weather cache for this place
                weatherRepository.updateWeatherCacheForFavoritePlace(
                    placeId = favoritePlaceInfo.id,
                    latitude = favoritePlaceInfo.latitude,
                    longitude = favoritePlaceInfo.longitude,
                    timeZone = favoritePlaceInfo.timeZone
                )
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        emit(
                            Response.Exception(
                                cause = Cause.UnknownException(
                                    message = e.message
                                )
                            )
                        )
                    }
                    is IOException -> {
                        emit(
                            Response.Exception(
                                cause = Cause.BadConnectionException
                            )
                        )
                    }
                }
            }
            val place = placesRepository.getFavoritePlaceByPlaceId(
                placeId = favoritePlaceId
            )
            if (place.weatherInfo != null) {
                // Get favorite place by id one more time (with updated cache) and emit success with it
                emit(
                    Response.Success(
                        data = place
                    )
                )
            }
        } else {
            // In case place is current place by geolocation
            locationTracker.getCurrentLocation()?.let { location ->
                // location found successfully

                // Get name of this place by reverse geocoding and create/update entity
                placesRepository.updateCurrentPlaceInfo(
                    latitude = location.latitude,
                    longitude = location.longitude
                )

                // Update weather cache for this place
                weatherRepository.updateWeatherCacheForFavoritePlace(
                    placeId = -2L,
                    latitude = location.latitude,
                    longitude = location.longitude,
                    timeZone = TimeZone.getDefault().id
                )

                val place = placesRepository.getFavoritePlaceByPlaceId(
                    placeId = -2L
                )
                if (place.weatherInfo != null) {
                    // Get favorite place by id one more time (with updated cache) and emit success with it
                    emit(
                        Response.Success(
                            data = placesRepository.getFavoritePlaceByPlaceId(
                                placeId = -2L
                            )
                        )
                    )
                }
            } ?: kotlin.run {
                // Case not found location
                emit(
                    Response.Exception(
                        cause = Cause.UnknownException(
                            message = "Cannot find location"
                        )
                    )
                )
            }
        }
    }
}
