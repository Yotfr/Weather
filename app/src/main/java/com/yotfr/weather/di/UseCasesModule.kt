package com.yotfr.weather.di

import com.yotfr.weather.domain.location.LocationTracker
import com.yotfr.weather.domain.repository.PlacesRepository
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
        weatherRepository: WeatherRepository
    ): AddPlaceToFavoriteUseCase {
        return AddPlaceToFavoriteUseCase(
            placesRepository = placesRepository,
            weatherRepository = weatherRepository
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
    fun provideGetWeatherInfoForFavoritePlace(
        weatherRepository: WeatherRepository,
        locationTracker: LocationTracker,
        placesRepository: PlacesRepository
    ): GetWeatherInfoForFavoritePlace {
        return GetWeatherInfoForFavoritePlace(
            weatherRepository = weatherRepository,
            locationTracker = locationTracker,
            placesRepository = placesRepository
        )
    }
}
