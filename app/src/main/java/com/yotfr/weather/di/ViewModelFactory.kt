package com.yotfr.weather.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yotfr.weather.presentation.favoriteplaces.FavoritePlacesViewModel
import com.yotfr.weather.presentation.searchplaces.SearchPlacesViewModel
import com.yotfr.weather.presentation.sevendaysforecast.SevenDaysForecastViewModel
import com.yotfr.weather.presentation.currentdayforecast.CurrentDayForecastViewModel
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    currentDayForecastViewModelProvider: Provider<CurrentDayForecastViewModel>,
    searchPlacesViewModelProvider: Provider<SearchPlacesViewModel>,
    favoritePlacesViewModelProvider: Provider<FavoritePlacesViewModel>,
    sevenDaysForecastViewModelProvider: Provider<SevenDaysForecastViewModel>
) : ViewModelProvider.Factory {
    private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
        CurrentDayForecastViewModel::class.java to currentDayForecastViewModelProvider,
        SearchPlacesViewModel::class.java to searchPlacesViewModelProvider,
        FavoritePlacesViewModel::class.java to favoritePlacesViewModelProvider,
        SevenDaysForecastViewModel::class.java to sevenDaysForecastViewModelProvider
    )
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return providers[modelClass]!!.get() as T
    }
}
