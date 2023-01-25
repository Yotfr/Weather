package com.yotfr.weather.presentation.favoriteplaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.weather.domain.usecases.DeleteFavoritePlaceUseCase
import com.yotfr.weather.domain.usecases.GetAllFavoritePlacesUseCase
import com.yotfr.weather.domain.usecases.GetTemperatureUnitUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritePlacesViewModel @Inject constructor(
    private val getAllFavoritePlacesUseCase: GetAllFavoritePlacesUseCase,
    private val deleteFavoritePlaceUseCase: DeleteFavoritePlaceUseCase,
    private val getTemperatureUnitUseCase: GetTemperatureUnitUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritePlacesState())
    val state = _state.asStateFlow()

    fun onEvent(event: FavoritePlacesEvent) {
        when (event) {
            is FavoritePlacesEvent.DeleteFavoritePlace -> {
                viewModelScope.launch {
                    deleteFavoritePlaceUseCase(
                        place = event.placeInfo
                    )
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            combine(
                getAllFavoritePlacesUseCase(),
                getTemperatureUnitUseCase()
            ) { placeList, temperatureUnit ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        rvList = placeList,
                        temperatureUnit = temperatureUnit
                    )
                }
            }.collect()
        }
    }
}
