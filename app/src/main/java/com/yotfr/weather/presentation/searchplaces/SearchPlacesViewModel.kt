package com.yotfr.weather.presentation.searchplaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.weather.domain.model.PlaceInfo
import com.yotfr.weather.domain.usecases.AddFavoritePlaceUseCase
import com.yotfr.weather.domain.usecases.SearchPlacesUseCase
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchPlacesViewModel @Inject constructor(
    private val searchPlacesUseCase: SearchPlacesUseCase,
    private val addFavoritePlaceUseCase: AddFavoritePlaceUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SearchPlacesState())
    val state = _state.asStateFlow()

    fun onEvent(event: SearchPlacesEvent) {
        when (event) {
            is SearchPlacesEvent.SearchQueryChanged -> {
                viewModelScope.launch {
                    if (event.newSearchQuery.length > 1) {
                        searchPlacesUseCase(
                            searchQuery = event.newSearchQuery
                        ).collectLatest { response ->
                            when (response) {
                                is Response.Loading<*> -> {
                                    _state.update {
                                        it.copy(
                                            isLoading = true
                                        )
                                    }
                                }
                                is Response.Success -> {
                                    if (response.data != null) {
                                        _state.update {
                                            it.copy(
                                                isLoading = false,
                                                rvList = response.data as List<PlaceInfo>
                                            )
                                        }
                                    }
                                }
                                is Response.Exception -> {
                                }
                            }
                        }
                    }
                }
            }
            is SearchPlacesEvent.AddPlaceToFavorite -> {
                viewModelScope.launch {
                    addFavoritePlaceUseCase(
                        place = event.place
                    )
                }
            }
        }
    }
}
