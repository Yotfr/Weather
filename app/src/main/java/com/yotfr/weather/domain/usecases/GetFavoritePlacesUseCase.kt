package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.repository.PlacesRepository
import com.yotfr.weather.domain.util.Cause
import com.yotfr.weather.domain.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetFavoritePlacesUseCase(
    private val placesRepository: PlacesRepository
) {
    suspend operator fun invoke(): Flow<Response<List<FavoritePlaceInfo>>> = flow {
        emit(Response.Loading())
        try {
            emit(
                Response.Success(
                    data = placesRepository.getFavoritePlaces()
                )
            )
        } catch (e: Exception) {
            emit(
                Response.Exception(
                    cause = Cause.UnknownException(
                        message = e.message
                    )
                )
            )
        }
    }
}
