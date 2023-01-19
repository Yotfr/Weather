package com.yotfr.weather.di

import android.app.Application
import android.content.Context
import com.yotfr.weather.presentation.places.citymanagement.CityManagementViewModel
import com.yotfr.weather.presentation.weather.weather_info.WeatherInfoViewModel
import dagger.BindsInstance
import dagger.Component
import dagger.Component.Builder
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun appContext(context: Context): Builder

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): AppComponent
    }

    fun viewModelFavtory(): ViewModelFactory

    fun weatherInfoViewModel(): WeatherInfoViewModel

    fun cityManagementViewModel(): CityManagementViewModel
}
