package com.yotfr.weather.di

import com.yotfr.weather.domain.location.LocationTracker
import com.yotfr.weather.domain.repository.PlacesRepository
import com.yotfr.weather.domain.repository.SettingsRepository
import com.yotfr.weather.domain.repository.WeatherRepository
import com.yotfr.weather.domain.usecases.*
import dagger.Module
import dagger.Provides

@Module
class UseCasesModule {

    @Provides
    fun provideSearchPlaceUseCase(
        placesRepository: PlacesRepository
    ): SearchPlacesUseCase {
        return SearchPlacesUseCase(
            placesRepository = placesRepository
        )
    }

    @Provides
    fun provideAddPlaceToFavoriteUseCase(
        placesRepository: PlacesRepository,
        weatherRepository: WeatherRepository,
        settingsRepository: SettingsRepository
    ): AddPlaceToFavoriteUseCase {
        return AddPlaceToFavoriteUseCase(
            placesRepository = placesRepository,
            weatherRepository = weatherRepository,
            settingsRepository = settingsRepository
        )
    }

    @Provides
    fun getFavoritePlacesUseCase(
        placesRepository: PlacesRepository
    ): GetFavoritePlacesUseCase {
        return GetFavoritePlacesUseCase(
            placesRepository = placesRepository
        )
    }

    @Provides
    fun getWeatherDataForSearchedPlaceUseCase(
        weatherRepository: WeatherRepository,
        settingsRepository: SettingsRepository
    ): GetWeatherDataForSearchedPlace {
        return GetWeatherDataForSearchedPlace(
            weatherRepository = weatherRepository,
            settingsRepository = settingsRepository
        )
    }

    @Provides
    fun getDeleteFavoritePlaceUseCase(
        placesRepository: PlacesRepository,
        weatherRepository: WeatherRepository
    ): DeleteFavoritePlaceUseCase {
        return DeleteFavoritePlaceUseCase(
            placesRepository = placesRepository,
            weatherRepository = weatherRepository
        )
    }

    @Provides
    fun getTemperatureUnitsUseCase(
        settingsRepository: SettingsRepository
    ): GetTemperatureUnitUseCase {
        return GetTemperatureUnitUseCase(
            settingsRepository = settingsRepository
        )
    }

    @Provides
    fun getWindSpeedUnitsUseCase(
        settingsRepository: SettingsRepository
    ): GetWindSpeedUnitUseCase {
        return GetWindSpeedUnitUseCase(
            settingsRepository = settingsRepository
        )
    }

    @Provides
    fun updateTemperatureUnitsUseCase(
        settingsRepository: SettingsRepository
    ): UpdateTemperatureUnitUseCase {
        return UpdateTemperatureUnitUseCase(
            settingsRepository = settingsRepository
        )
    }

    @Provides
    fun updateWindSpeedUnitsUseCase(
        settingsRepository: SettingsRepository
    ): UpdateWindSpeedUnitsUseCase {
        return UpdateWindSpeedUnitsUseCase(
            settingsRepository = settingsRepository
        )
    }

    @Provides
    fun provideGetWeatherInfoForFavoritePlace(
        weatherRepository: WeatherRepository,
        locationTracker: LocationTracker,
        placesRepository: PlacesRepository,
        settingsRepository: SettingsRepository
    ): GetWeatherInfoForFavoritePlace {
        return GetWeatherInfoForFavoritePlace(
            weatherRepository = weatherRepository,
            locationTracker = locationTracker,
            placesRepository = placesRepository,
            settingsRepository = settingsRepository
        )
    }
}
