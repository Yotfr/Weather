package com.yotfr.weather.presentation.weather_info

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.weather.domain.usecase.LoadWeatherInfoUseCase
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherInfoViewModel @Inject constructor(
    private val loadWeatherInfoUseCase: LoadWeatherInfoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherInfoState())
    val state = _state.asStateFlow()

    init {
        loadWeatherData()
    }

    private fun loadWeatherData() {
        viewModelScope.launch {
            loadWeatherInfoUseCase().collectLatest { response ->
                Log.d("TEST", "respond $response")
                when (response) {
                    is Response.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Response.Succecss -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                weatherInfo = response.data
                            )
                        }
                    }
                    is Response.Error -> {
                    }
                }
            }
        }
    }
}
