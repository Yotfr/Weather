package com.yotfr.weather.di

import com.yotfr.weather.domain.location.LocationTracker
import com.yotfr.weather.domain.repository.PlacesRepository
import com.yotfr.weather.domain.repository.WeatherRepository
import com.yotfr.weather.domain.usecases.AddPlaceToFavoriteUseCase
import com.yotfr.weather.domain.usecases.GetFavoritePlacesUseCase
import com.yotfr.weather.domain.usecases.LoadWeatherInfoUseCase
import com.yotfr.weather.domain.usecases.SearchPlacesUseCase
import dagger.Module
import dagger.Provides

@Module
class UseCasesModule {

    @Provides
    fun provideLoadWeatherInfoUseCase(
        weatherRepository: WeatherRepository,
        locationTracker: LocationTracker
    ): LoadWeatherInfoUseCase {
        return LoadWeatherInfoUseCase(
            weatherRepository = weatherRepository,
            locationTracker = locationTracker
        )
    }

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
        placesRepository: PlacesRepository
    ): AddPlaceToFavoriteUseCase {
        return AddPlaceToFavoriteUseCase(
            placesRepository = placesRepository
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
}
