package com.yotfr.weather.presentation.favoriteplaces

import android.util.Log
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
        Log.d("TEST", "init")
        viewModelScope.launch {
            getFavoritePlacesUseCase().collectLatest { placeList ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        rvList = placeList
                    )
                }
            }
        }
    }
}
