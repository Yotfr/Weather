package com.yotfr.weather.di

import com.yotfr.weather.data.weather.location.DefLocationTracker
import com.yotfr.weather.data.places.repository.LocationRepositoryImpl
import com.yotfr.weather.data.weather.repository.WeatherRepositoryImpl
import com.yotfr.weather.domain.weather.location.LocationTracker
import com.yotfr.weather.domain.places.repository.LocationRepository
import com.yotfr.weather.domain.weather.repository.WeatherRepository
import dagger.Binds
import dagger.Module

@Module
abstract class AppBindModule {

    @Binds
    abstract fun bindDefLocationTracker_to_LocationTracker(
        defLocationTracker: DefLocationTracker
    ): LocationTracker

    @Binds
    abstract fun bindWeatherRepositoryImpl_to_WeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    abstract fun bindLocationRepositoryImpl_to_LocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository
}
