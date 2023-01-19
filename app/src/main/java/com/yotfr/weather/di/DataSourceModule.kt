package com.yotfr.weather.di

import android.content.Context
import androidx.room.Room
import com.yotfr.weather.data.datasource.local.WeatherCacheDao
import com.yotfr.weather.data.datasource.local.AppDataBase
import com.yotfr.weather.data.datasource.local.PlacesDao
import com.yotfr.weather.data.datasource.remote.PlacesApi
import com.yotfr.weather.data.datasource.remote.WeatherApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

private const val WEATHER_BASE_URL = "https://api.open-meteo.com/"
private const val LOCATION_BASE_URL = "https://geocoding-api.open-meteo.com/"

@Module
class DataSourceModule {

    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi {
        return Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideLocationApi(): PlacesApi {
        return Retrofit.Builder()
            .baseUrl(LOCATION_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideWeatherDataBase(context: Context): AppDataBase {
        return Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "weather_db"
        ).build()
    }

    @Provides
    fun provideWeatherDao(appDataBase: AppDataBase): WeatherCacheDao {
        return appDataBase.weatherCacheDao
    }

    @Provides
    fun providePlacesDao(appDataBase: AppDataBase): PlacesDao {
        return appDataBase.placesDao
    }
}
