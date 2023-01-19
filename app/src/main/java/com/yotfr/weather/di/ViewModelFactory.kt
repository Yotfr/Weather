package com.yotfr.weather.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yotfr.weather.presentation.places.citymanagement.CityManagementViewModel
import com.yotfr.weather.presentation.weather.weather_info.WeatherInfoViewModel
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    weatherInfoViewModelProvider: Provider<WeatherInfoViewModel>,
    cityManagementViewModelProvider: Provider<CityManagementViewModel>
) : ViewModelProvider.Factory {
    private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
        WeatherInfoViewModel::class.java to weatherInfoViewModelProvider,
        CityManagementViewModel::class.java to cityManagementViewModelProvider
    )
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return providers[modelClass]!!.get() as T
    }
}
