package com.yotfr.weather.presentation.favoriteplaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.usecases.GetFavoritePlacesUseCase
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritePlacesViewModel @Inject constructor(
    private val getFavoritePlacesUseCase: GetFavoritePlacesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritePlacesState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getFavoritePlacesUseCase().collectLatest { response ->
                when (response) {
                    is Response.Loading -> {
                       // TODO loading state
                    }
                    is Response.Success -> {
                        if (response.data != null) {
                            _state.update { state ->
                                state.copy(
                                    isLoading = false,
                                    rvList = response.data
                                )
                            }
                        }
                    }
                    is Response.Exception -> {
                        // TODO exception state
                    }
                }
            }
        }
    }
}
