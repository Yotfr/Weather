package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.model.PlaceInfo
import com.yotfr.weather.domain.repository.SettingsRepository
import com.yotfr.weather.domain.repository.WeatherRepository
import com.yotfr.weather.domain.util.Cause
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException

class GetWeatherDataForSearchedPlace(
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(place: PlaceInfo): Flow<Response<FavoritePlaceInfo>> = flow {
        emit(Response.Loading())
        try {
            val temperatureUnits = settingsRepository.getTemperatureUnits(overload = true)
            val windSpeedUnits = settingsRepository.getWindSpeedUnits(overload = true)

            val weatherInfo = weatherRepository.getWeatherDataForSearchedPlace(
                latitude = place.latitude,
                longitude = place.longitude,
                timeZone = place.timeZone,
                temperatureUnits = temperatureUnits.stringName,
                windSpeedUnits = windSpeedUnits.stringName
            )
            emit(Response.Success(
                data = FavoritePlaceInfo(
                    id = place.id,
                    placeName = place.placeName,
                    latitude = place.latitude,
                    longitude = place.longitude,
                    countryName = place.countryName,
                    timeZone = place.timeZone,
                    weatherInfo = weatherInfo
                )
            ))
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
    }
}
