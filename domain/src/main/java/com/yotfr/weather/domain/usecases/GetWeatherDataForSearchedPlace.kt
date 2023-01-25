package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.model.PlaceInfo
import com.yotfr.weather.domain.repository.SettingsRepository
import com.yotfr.weather.domain.repository.WeatherRepository
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * [GetWeatherDataForSearchedPlace] returns information about the searched place and
 * information about the weather in it
 */
class GetWeatherDataForSearchedPlace(
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(placeInfo: PlaceInfo): Flow<Response<FavoritePlaceInfo>> = flow {
        emit(Response.Loading())
        // Get currently selected measuring units
        val measuringUnits = settingsRepository.getMeasuringUnitsValues()

        // Get weather data for selected place but not save it to the database
        val getWeatherDataResponse = weatherRepository.getWeatherDataForSearchedPlace(
            placeInfo = placeInfo,
            temperatureUnits = measuringUnits.temperatureUnit.stringName,
            windSpeedUnits = measuringUnits.windSpeedUnit.stringName
        )
        emit(getWeatherDataResponse)
    }
}
