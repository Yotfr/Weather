package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.location.LocationTracker
import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.repository.PlacesRepository
import com.yotfr.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import java.util.TimeZone

class GetWeatherInfoForFavoritePlace(
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker,
    private val placesRepository: PlacesRepository
) {
    suspend operator fun invoke(favoritePlaceId: Long?): Flow<FavoritePlaceInfo> {
        if (favoritePlaceId != null) {

            val favoritePlaceInfo = placesRepository.getFavoritePlaceByPlaceId(
                placeId = favoritePlaceId
            ).last()

            weatherRepository.updateWeatherCacheForFavoritePlace(
                placeId = favoritePlaceInfo.id,
                latitude = favoritePlaceInfo.latitude,
                longitude = favoritePlaceInfo.longitude,
                timeZone = favoritePlaceInfo.timeZone
            )
        } else {
            locationTracker.getCurrentLocation()?.let { location ->
                weatherRepository.updateWeatherCacheForFavoritePlace(
                    placeId = 0L,
                    latitude = location.latitude,
                    longitude = location.longitude,
                    timeZone = TimeZone.getDefault().id
                )
            }
        }
        return placesRepository.getFavoritePlaceByPlaceId(
            placeId = favoritePlaceInfo.id
        )
    }
}
