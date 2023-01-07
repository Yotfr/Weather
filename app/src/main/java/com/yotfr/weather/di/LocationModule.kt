package com.yotfr.weather.di

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yotfr.weather.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocationModule {

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(app: App): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(app)
    }
}
