package com.yotfr.weather.presentation.places.citymanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yotfr.weather.domain.places.usecases.SearchLocationUseCase
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CityManagementViewModel @Inject constructor(
    private val searchLocationUseCase: SearchLocationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CityManagementState())
    val state = _state.asStateFlow()

    fun onEvent(event: CityManagementEvent) {
        when (event) {
            is CityManagementEvent.SearchQueryChanged -> {
                viewModelScope.launch {
                    if (event.newSearchQuery.length > 1) {
                        searchLocationUseCase(
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
                                    _state.update {
                                        it.copy(
                                            isLoading = false,
                                            rvList = response.data
                                        )
                                    }
                                }
                                is Response.Exception -> {
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
