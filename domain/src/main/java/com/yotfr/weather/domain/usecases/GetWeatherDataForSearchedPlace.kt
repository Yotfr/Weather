package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.model.PlaceInfo
import com.yotfr.weather.domain.repository.SettingsRepository
import com.yotfr.weather.domain.repository.WeatherRepository
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWeatherDataForSearchedPlace(
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(placeInfo: PlaceInfo): Flow<Response<FavoritePlaceInfo>> = flow {
        emit(Response.Loading())

        val temperatureUnits = settingsRepository.getTemperatureUnits(overload = true)
        val windSpeedUnits = settingsRepository.getWindSpeedUnits(overload = true)

        val getWeatherDataResponse = weatherRepository.getWeatherDataForSearchedPlace(
            placeInfo = placeInfo,
            temperatureUnits = temperatureUnits.stringName,
            windSpeedUnits = windSpeedUnits.stringName
        )
        emit(getWeatherDataResponse)
    }
}
