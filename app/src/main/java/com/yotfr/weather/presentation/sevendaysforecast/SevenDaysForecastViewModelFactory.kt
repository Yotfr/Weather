package com.yotfr.weather.presentation.sevendaysforecast

import androidx.lifecycle.SavedStateHandle
import com.yotfr.weather.di.ViewModelAssistedFactory
import com.yotfr.weather.domain.usecases.LoadWeatherInfoUseCase
import javax.inject.Inject

class SevenDaysForecastViewModelFactory @Inject constructor(
    private val getWeatherInfoUseCase: LoadWeatherInfoUseCase
) : ViewModelAssistedFactory<SevenDaysForecastViewModel> {
    override fun create(handle: SavedStateHandle): SevenDaysForecastViewModel {
        return SevenDaysForecastViewModel(getWeatherInfoUseCase, handle)
    }
}