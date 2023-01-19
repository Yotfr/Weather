package com.yotfr.weather.data.repository

import com.yotfr.weather.data.datasource.remote.WeatherApi
import com.yotfr.weather.data.datasource.remote.dto.HourlyWeatherDataDto
import com.yotfr.weather.data.datasource.remote.dto.WeatherDto
import com.yotfr.weather.domain.repository.WeatherRepository
import com.yotfr.weather.domain.util.Cause
import com.yotfr.weather.domain.util.Response
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okio.IOException
import org.junit.Assert.*
import org.junit.Test
import retrofit2.HttpException

@OptIn(ExperimentalCoroutinesApi::class)
class TestWeatherRepositoryImpl {

    @Test
    fun `getWeatherData must return Response Success with data if result from api is WeatherDTO`() =
        runTest {
            val weatherApi = createWeatherApi()
            val weatherRepository = createWeatherRepository(weatherApi)
            coEvery { weatherApi.getWeatherData(any(), any(), any()) } coAnswers {
                WeatherDto(
                    hourlyWeatherData = HourlyWeatherDataDto(
                        time = emptyList(),
                        temperatures = emptyList(),
                        weatherCodes = emptyList(),
                        pressures = emptyList(),
                        windSpeeds = emptyList(),
                        humidities = emptyList()
                    )
                )
            }

            val result = weatherRepository.getWeatherData(0.0, 0.0)

            assertEquals(Response.Success::class.java, result.last().javaClass)

            val success = result.last() as Response.Success

            assertNotNull(success.data)
        }

    @Test
    fun `getWeatherData must return Response Exception with UnknownException Cause if result from api is Error code 400`() =
        runTest {
            val weatherApi = createWeatherApi()
            val weatherRepository = createWeatherRepository(weatherApi)

            coEvery { weatherApi.getWeatherData(any(), any(), any()) } throws
                HttpException(
                    retrofit2.Response.error<ResponseBody>(
                        400,
                        ResponseBody.create("plain/text".toMediaType(), "")
                    )
                )

            val result = weatherRepository.getWeatherData(0.0, 0.0)

            assertEquals(Response.Exception::class.java, result.last().javaClass)

            val cause = result.last() as Response.Exception

            assertEquals(Cause.UnknownException::class.java, cause.cause.javaClass)
        }

    @Test
    fun `getWeatherData must return Response Exception with UnknownException Cause if result from api differs from code 400`() =
        runTest {
            val weatherApi = createWeatherApi()
            val weatherRepository = createWeatherRepository(weatherApi)

            coEvery { weatherApi.getWeatherData(any(), any(), any()) } throws
                HttpException(
                    retrofit2.Response.error<ResponseBody>(
                        500,
                        ResponseBody.create("plain/text".toMediaType(), "")
                    )
                )

            val result = weatherRepository.getWeatherData(0.0, 0.0)

            assertEquals(Response.Exception::class.java, result.last().javaClass)

            val cause = result.last() as Response.Exception

            assertEquals(Cause.UnknownException::class.java, cause.cause.javaClass)
        }

    @Test
    fun `getWeatherData must return Response Exception with BadConnectionException Cause if IOException occurs`() =
        runTest {
            val weatherApi = createWeatherApi()
            val weatherRepository = createWeatherRepository(weatherApi)

            coEvery { weatherApi.getWeatherData(any(), any(), any()) } throws IOException()

            val result = weatherRepository.getWeatherData(0.0, 0.0)

            assertEquals(Response.Exception::class.java, result.last().javaClass)

            val cause = result.last() as Response.Exception

            assertEquals(Cause.BadConnectionException::class.java, cause.cause.javaClass)
        }

    @Test
    fun `getWeatherData must return Response Exception with UnknownException Cause if unknown exception occurs`() =
        runTest {
            val weatherApi = createWeatherApi()
            val weatherRepository = createWeatherRepository(weatherApi)

            coEvery { weatherApi.getWeatherData(any(), any(), any()) } throws Exception()

            val result = weatherRepository.getWeatherData(0.0, 0.0)

            assertEquals(Response.Exception::class.java, result.last().javaClass)

            val cause = result.last() as Response.Exception

            assertEquals(Cause.UnknownException::class.java, cause.cause.javaClass)
        }

    @Test
    fun `getWeatherData must always return Loading State first`() = runTest {
        val weatherApi = createWeatherApi()
        val weatherRepository = createWeatherRepository(weatherApi)

        coEvery { weatherApi.getWeatherData(any(), any(), any()) } throws Exception()

        val result = weatherRepository.getWeatherData(0.0, 0.0)

        assertEquals(Response.Loading::class.java, result.first().javaClass)
    }

    private fun createMockedBlock(): suspend () -> String {
        return mockk()
    }

    private fun createWeatherApi(): WeatherApi {
        return mockk()
    }

    private fun createWeatherRepository(weatherApi: WeatherApi): WeatherRepository {
        return WeatherRepositoryImpl(weatherApi)
    }
}
