package com.yotfr.weather.presentation.favoriteplaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.weather.domain.usecases.GetFavoritePlacesUseCase
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
            getFavoritePlacesUseCase().collectLatest { places ->
                _state.update {
                    it.copy(
                        rvList = places
                    )
                }
            }
        }
    }
}
