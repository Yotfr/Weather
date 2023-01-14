package com.yotfr.weather.data.repository

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.yotfr.weather.data.datasource.remote.WeatherApi
import com.yotfr.weather.domain.repository.WeatherRepository
import com.yotfr.weather.testutils.TestWeatherInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import java.net.HttpURLConnection
import java.util.TimeZone

@OptIn(ExperimentalCoroutinesApi::class)
class TestWeatherRepositoryImpl {

    private lateinit var repository: WeatherRepository
    private lateinit var weatherApi: WeatherApi
    private lateinit var mockWebServer: MockWebServer
    private lateinit var moshi: Moshi

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        weatherApi = RetrofitHelper.weatherApiInstance(mockWebServer.url("/").toString())
        repository = WeatherRepositoryImpl(weatherApi)
        moshi = RetrofitHelper.moshiInstance()
    }

    @After
    fun shutDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `get weather data must return WeatherInfo with code 200`() = runTest {
        val testWeatherInfo = TestWeatherInfo()

        val jsonAdapter: JsonAdapter<TestWeatherInfo> = moshi.adapter(TestWeatherInfo::class.java)

        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(jsonAdapter.toJson(testWeatherInfo))
        mockWebServer.enqueue(expectedResponse)

        val actualResponse = weatherApi.getWeatherData(
            0.0,
            0.0,
            TimeZone.getDefault().id
        )

        assertEquals(actualResponse, expectedResponse)
    }
}

object RetrofitHelper {

    fun moshiInstance(): Moshi {
        return Moshi.Builder().build()
    }

    fun weatherApiInstance(baseUrl: String): WeatherApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .build()
            .create(WeatherApi::class.java)
    }
}
