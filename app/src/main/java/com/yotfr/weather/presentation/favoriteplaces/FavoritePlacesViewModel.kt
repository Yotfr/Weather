package com.yotfr.weather.presentation.favoriteplaces

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.model.MeasuringUnits
import com.yotfr.weather.domain.usecases.*
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritePlacesViewModel @Inject constructor(
    private val getAllFavoritePlacesUseCase: GetAllFavoritePlacesUseCase,
    private val deleteFavoritePlaceUseCase: DeleteFavoritePlaceUseCase,
    private val getMeasuringUnitsUseCase: GetMeasuringUnitsUseCase
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
            FavoritePlacesEvent.Swiped -> {
                getFavoritePlaces()
            }
        }
    }

    init {
        getFavoritePlaces()
    }

    private fun getFavoritePlaces() {
        viewModelScope.launch {
            combine(
                getAllFavoritePlacesUseCase(),
                getMeasuringUnitsUseCase()
            ) { response, measuringUnits ->
                Log.d("TEST","response $response")
                when (response) {
                    is Response.Loading -> {
                        if (response.data == null) {
                            processLoadingStateWithoutData(measuringUnits)
                        } else {
                            processLoadingStateWithData(response, measuringUnits)
                        }
                    }
                    is Response.Success -> {
                        processSuccessState(response, measuringUnits)
                    }
                    is Response.Exception -> {
                        processExceptionState()
                    }
                }
            }.collect()
        }
    }

    private fun processExceptionState() {
        _state.update {
            it.copy(
                isLoading = false
            )
        }
    }

    private fun processSuccessState(
        response: Response<List<FavoritePlaceInfo>>,
        measuringUnits: MeasuringUnits
    ) {
        _state.update {
            it.copy(
                isLoading = false,
                rvList = response.data as List<FavoritePlaceInfo>,
                temperatureUnit = measuringUnits.temperatureUnit
            )
        }
    }

    private fun processLoadingStateWithData(
        response: Response<List<FavoritePlaceInfo>>,
        measuringUnits: MeasuringUnits
    ) {
        _state.update {
            it.copy(
                isLoading = true,
                rvList = response.data as List<FavoritePlaceInfo>,
                temperatureUnit = measuringUnits.temperatureUnit
            )
        }
    }

    private fun processLoadingStateWithoutData(measuringUnits: MeasuringUnits) {
        _state.update {
            it.copy(
                isLoading = true,
                temperatureUnit = measuringUnits.temperatureUnit
            )
        }
    }
}
