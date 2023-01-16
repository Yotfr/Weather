package com.yotfr.weather.di

import android.content.Context
import androidx.room.Room
import com.yotfr.weather.data.datasource.local.WeatherDao
import com.yotfr.weather.data.datasource.local.WeatherDataBase
import com.yotfr.weather.data.datasource.remote.WeatherApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

private const val BASE_URL = "https://api.open-meteo.com/"

@Module
class DataSourceModule {

    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideWeatherDataBase(context: Context) : WeatherDataBase {
        return Room.databaseBuilder(
            context,
            WeatherDataBase::class.java,
            "weather_db"
        ).build()
    }

    @Provides
    fun provideWeatherDao(weatherDataBase: WeatherDataBase): WeatherDao {
        return weatherDataBase.weatherDao
    }
}
