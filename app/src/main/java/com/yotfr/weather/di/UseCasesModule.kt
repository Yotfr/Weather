package com.yotfr.weather.di

import com.yotfr.weather.domain.weather.location.LocationTracker
import com.yotfr.weather.domain.places.repository.LocationRepository
import com.yotfr.weather.domain.weather.repository.WeatherRepository
import com.yotfr.weather.domain.weather.usecases.LoadWeatherInfoUseCase
import com.yotfr.weather.domain.places.usecases.SearchLocationUseCase
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
    fun provideSearchLocationCase(
        locationRepository: LocationRepository
    ): SearchLocationUseCase {
        return SearchLocationUseCase(
            locationRepository = locationRepository
        )
    }
}
