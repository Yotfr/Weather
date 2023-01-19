package com.yotfr.weather.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yotfr.weather.presentation.favoriteplaces.FavoritePlacesViewModel
import com.yotfr.weather.presentation.searchplaces.SearchPlacesViewModel
import com.yotfr.weather.presentation.weatherinfo.WeatherInfoViewModel
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    weatherInfoViewModelProvider: Provider<WeatherInfoViewModel>,
    searchPlacesViewModelProvider: Provider<SearchPlacesViewModel>,
    favoritePlacesViewModelProvider: Provider<FavoritePlacesViewModel>
) : ViewModelProvider.Factory {
    private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
        WeatherInfoViewModel::class.java to weatherInfoViewModelProvider,
        SearchPlacesViewModel::class.java to searchPlacesViewModelProvider,
        FavoritePlacesViewModel::class.java to favoritePlacesViewModelProvider
    )
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return providers[modelClass]!!.get() as T
    }
}
