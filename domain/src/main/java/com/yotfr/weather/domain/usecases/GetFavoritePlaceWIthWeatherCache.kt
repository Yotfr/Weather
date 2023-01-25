package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.location.LocationTracker
import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.repository.PlacesRepository
import com.yotfr.weather.domain.repository.SettingsRepository
import com.yotfr.weather.domain.repository.WeatherRepository
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.*
import java.util.TimeZone

/**
 * [GetFavoritePlaceWIthWeatherCache] returns information about the favorite place and
 * information about the weather in it
 *
 * NOTE: the user's current location is also considered a favorite place
 */
class GetFavoritePlaceWIthWeatherCache(
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker,
    private val placesRepository: PlacesRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(favoritePlaceId: Long): Flow<Response<FavoritePlaceInfo>> = flow {
        emit(Response.Loading())

        if (favoritePlaceId != -2L) {
            // That means that selected favorite place is not user's current location

            /*
             This function returns favorite place with non-updated weather cache
             Param [isCacheUpdated] determines whether the cache was updated
              at the time the function was called
             */
            val getFavoritePlaceResponse = placesRepository.getFavoritePlace(
                placeId = favoritePlaceId,
                isCacheUpdated = false
            )
            emit(getFavoritePlaceResponse)

            /*
            Following block of code is unreachable if the previous function returns Exception
            response or response with no data
             */
            getFavoritePlaceResponse.data?.let { favoritePlaceInfo ->

                // Get currently selected measuring units
                val measuringUnits = settingsRepository.getMeasuringUnitsValues()

                // Get weather data for this place and update the database table WeatherCache
                val updateWeatherCacheResponse = weatherRepository.updateWeatherCacheRelatedToPlaceId(
                    placeId = favoritePlaceInfo.id,
                    latitude = favoritePlaceInfo.latitude,
                    longitude = favoritePlaceInfo.longitude,
                    timeZone = favoritePlaceInfo.timeZone,
                    temperatureUnits = measuringUnits.temperatureUnit.stringName,
                    windSpeedUnits = measuringUnits.windSpeedUnit.stringName
                )

                /*
                 If weather cache successfully updated [updateWeatherCacheResponse] will be null,
                 following block of code will be executed only if [updateWeatherCacheResponse]
                 returns response with Exception
                 */
                updateWeatherCacheResponse?.let {
                    emit(it)
                }

                /*
                This function returns favorite place with updated weather cache
                Param [isCacheUpdated] determines whether the cache was updated
                at the time the function was called
                */
                val getFavoritePlaceResponseCacheUpdated = placesRepository.getFavoritePlace(
                    placeId = favoritePlaceId,
                    isCacheUpdated = true
                )
                emit(getFavoritePlaceResponseCacheUpdated)
            }
        } else {
            // That means that selected favorite place is the user's current location

            /*
             This function returns favorite place with non-updated weather cache
             Param [isCacheUpdated] determines whether the cache was updated
              at the time the function was called
             */
            val getFavoritePlaceResponse = placesRepository.getFavoritePlace(
                placeId = -2L,
                isCacheUpdated = false
            )
            emit(getFavoritePlaceResponse)

            // Get location coordinates
            val locationResponse = locationTracker.getCurrentLocation()

            /*
            Following block of code is unreachable if the previous function returns Exception
            response or response with no data (if the user declined permissions request,
            turn off the internet or gps)
             */
            locationResponse?.data?.let { location ->

                /*
                This function updates current user's location info (place with id -2L)
                 */
                val updateCurrentPlaceResponse = placesRepository.updateCurrentPlaceInfo(
                    latitude = location.latitude,
                    longitude = location.longitude
                )

                /*
                 If place info successfully updated [updateCurrentPlaceResponse] will be null,
                 following block of code will be executed only if [updateCurrentPlaceResponse]
                 returns response with Exception
                 */
                updateCurrentPlaceResponse?.let {
                    emit(updateCurrentPlaceResponse)
                }

                // Get currently selected measuring units
                val measuringUnits = settingsRepository.getMeasuringUnitsValues()

                // Get weather data for this place and update the database table WeatherCache
                val updateWeatherCacheResponse = weatherRepository.updateWeatherCacheRelatedToPlaceId(
                    placeId = -2L,
                    latitude = location.latitude,
                    longitude = location.longitude,
                    timeZone = TimeZone.getDefault().id,
                    temperatureUnits = measuringUnits.temperatureUnit.stringName,
                    windSpeedUnits = measuringUnits.windSpeedUnit.stringName
                )

                /*
                 If weather cache successfully updated [updateWeatherCacheResponse] will be null,
                 following block of code will be executed only if [updateWeatherCacheResponse]
                 returns response with Exception
                 */
                updateWeatherCacheResponse?.let {
                    emit(it)
                }

                /*
                This function returns favorite place with updated weather cache
                Param [isCacheUpdated] determines whether the cache was updated
                at the time the function was called
                */
                val getFavoritePlaceResponseCacheUpdated = placesRepository.getFavoritePlace(
                    placeId = -2L,
                    isCacheUpdated = true
                )
                emit(getFavoritePlaceResponseCacheUpdated)
            }
        }
    }
}
