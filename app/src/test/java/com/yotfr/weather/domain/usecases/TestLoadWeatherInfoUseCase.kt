package com.yotfr.weather.domain.usecases

import com.yotfr.weather.domain.weather.location.LocationTracker
import com.yotfr.weather.domain.weather.repository.WeatherRepository
import com.yotfr.weather.domain.weather.usecases.LoadWeatherInfoUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TestLoadWeatherInfoUseCase {

    @Test
    fun `loadWeatherInfoUseCase should return a flow from repository if location tracker returns location`() = runTest {
        val useCase = createLoadWeatherInfoUseCase()
        val repository = createWeatherRepository()
        val locationTracker = createLocationTracker()

        coEvery { locationTracker.getCurrentLocation() }
    }

    private fun createLoadWeatherInfoUseCase(): LoadWeatherInfoUseCase {
        return LoadWeatherInfoUseCase(
            weatherRepository = createWeatherRepository(),
            locationTracker = createLocationTracker()
        )
    }

    private fun createWeatherRepository(): WeatherRepository {
        return mockk()
    }

    private fun createLocationTracker(): LocationTracker {
        return mockk()
    }

    data class TestLocation(
        val latitude: Double = 0.0,
        val longitude: Double = 0.0
    )
}
