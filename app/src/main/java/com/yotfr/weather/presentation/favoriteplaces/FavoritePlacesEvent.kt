package com.yotfr.weather.presentation.favoriteplaces

import com.yotfr.weather.domain.model.FavoritePlaceInfo

sealed interface FavoritePlacesEvent {
    data class DeleteFavoritePlace(val placeInfo: FavoritePlaceInfo) : FavoritePlacesEvent
}
