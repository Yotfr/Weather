package com.yotfr.weather.di

import android.app.Application
import android.content.Context
import com.yotfr.weather.presentation.favoriteplaces.FavoritePlacesViewModel
import com.yotfr.weather.presentation.searchplaces.SearchPlacesViewModel
import com.yotfr.weather.presentation.weatherinfo.WeatherInfoViewModel
import dagger.BindsInstance
import dagger.Component
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

    fun cityManagementViewModel(): SearchPlacesViewModel

    fun favoritePlacesViewModel(): FavoritePlacesViewModel
}
