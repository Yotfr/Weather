package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.repository.PlacesRepository
import com.yotfr.weather.domain.repository.SettingsRepository
import com.yotfr.weather.domain.repository.WeatherRepository
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * [GetAllFavoritePlacesUseCase] returns information about all favorite places
 */
class GetAllFavoritePlacesUseCase(
    private val placesRepository: PlacesRepository,
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): Flow<Response<List<FavoritePlaceInfo>>> =
        flow<Response<List<FavoritePlaceInfo>>> {
            emit(Response.Loading())

            /*
                 Get all favorite places from the database.
                 Param [isCacheUpdated] determines whether the places was updated
                 at the time the function was called
                 */
            val nonUpdatedFavoritePlacesResponse = placesRepository.getFavoritePlaces(
                isUpdated = false
            )
            println(" places non update $nonUpdatedFavoritePlacesResponse")
            emit(nonUpdatedFavoritePlacesResponse)

            // Get currently selected measuring units
            val measuringUnits = settingsRepository.getMeasuringUnitsValues()
            println(" measuringUnits $measuringUnits")

            /*
                 Following block of code is unreachable if the nonUpdatedFavoritePlacesResponse is Exception
                 response or response with no data
                 */
            nonUpdatedFavoritePlacesResponse.data?.let { favoritePlaces ->

                favoritePlaces.forEach { favoritePlace ->

                    if (favoritePlace.id == -2L) {
                        // That means that selected favorite place is not user's current location

                        // Update current place info (geolocation or language could changed)
                        val updateCurrentPlaceResponse = placesRepository.updateCurrentPlaceInfo(
                            latitude = favoritePlace.latitude,
                            longitude = favoritePlace.longitude
                        )
                        println(" updatePlaceResponse $updateCurrentPlaceResponse")

                        /*
                             Previous function returns response of incompatible type, but we need only
                             Exception response from it. (If update successful it returns null).
                             Solution is to get exception cause from response with incompatible type
                             and emit response exception with required type
                             */
                        updateCurrentPlaceResponse?.let { response ->
                            if (response is Response.Exception) {
                                response.cause?.let { cause ->
                                    emit(
                                        Response.Exception(
                                            cause = cause
                                        )
                                    )
                                }
                            }
                        }
                    } else {
                        // That means that selected favorite place is the user's current location

                        // Update favorite place info (language could changed)
                        val updatePlaceResponse = placesRepository.updateFavoritePlaceInfo(
                            placeId = favoritePlace.id
                        )
                        println(" updatePlaceResponse2 $updatePlaceResponse")

                        /*
                             If place info successfully updated [updatePlaceResponse] will be null,
                             following block of code will be executed only if [updatePlaceResponse]
                             returns response with Exception
                             */
                        updatePlaceResponse?.let {
                            emit(it)
                        }
                    }

                    // Get weather data for this place and update the database table WeatherCache
                    val updatedWeatherCacheResponse =
                        weatherRepository.updateWeatherCacheRelatedToPlaceId(
                            placeId = favoritePlace.id,
                            latitude = favoritePlace.latitude,
                            longitude = favoritePlace.longitude,
                            timeZone = favoritePlace.timeZone,
                            temperatureUnits = measuringUnits.temperatureUnit.stringName,
                            windSpeedUnits = measuringUnits.windSpeedUnit.stringName
                        )

                    println(" updateWeather $updatedWeatherCacheResponse")

                    /*
                         Previous function returns response of incompatible type, but we need only
                         Exception response from it. (If update successful it returns null).
                         Solution is to get exception cause from response with incompatible type
                         and emit response exception with required type
                         */
                    updatedWeatherCacheResponse?.let { response ->
                        if (response is Response.Exception) {
                            response.cause?.let { cause ->
                                emit(
                                    Response.Exception(
                                        cause = cause
                                    )
                                )
                            }
                        }
                    }
                }

                /*
                     Get all favorite places from the database.
                     Param [isCacheUpdated] determines whether the places was updated
                     at the time the function was called
                     */
                val updatedFavoritePlacesResponse = placesRepository.getFavoritePlaces(
                    isUpdated = true
                )
                println(" places update $updatedFavoritePlacesResponse")
                emit(updatedFavoritePlacesResponse)
            }
        }.flowOn(Dispatchers.IO)
}
