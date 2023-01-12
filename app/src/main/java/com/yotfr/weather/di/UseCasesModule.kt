package com.yotfr.weather.di

import com.yotfr.weather.domain.location.LocationTracker
import com.yotfr.weather.domain.repository.WeatherRepository
import com.yotfr.weather.domain.usecases.LoadWeatherInfoUseCase
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
}
