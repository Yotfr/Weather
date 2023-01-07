package com.yotfr.weather.data.mapper

import com.yotfr.weather.data.remote.WeatherDataDto
import com.yotfr.weather.data.remote.WeatherDto
import com.yotfr.weather.domain.model.WeatherData
import com.yotfr.weather.domain.model.WeatherInfo
import com.yotfr.weather.domain.model.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @param[index] is an hour of week
 * @param[index] is a weather information for hour with [index]
 */
private data class IndexedWeatherData(
    val index: Int,
    val data: WeatherData
)

/**
 * Api returns weather information for hours of week without dividing into days, so we need to
 * divide index by 24
 */
fun WeatherDataDto.mapToWeatherData(): Map<Int, List<WeatherData>> {
    return time.mapIndexed { index, time ->
        val temperature = temperatures[index]
        val weatherCode = weatherCodes[index]
        val windSpeed = windSpeeds[index]
        val pressure = pressures[index]
        val humidity = humidities[index]
        IndexedWeatherData(
            index = index,
            data = WeatherData(
                time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                temperature = temperature,
                pressure = pressure,
                windSpeed = windSpeed,
                humidity = humidity,
                weatherType = WeatherType.fromWMO(weatherCode)
            )
        )
    }.groupBy {
        it.index / 24
    }.mapValues { values ->
        values.value.map { it.data }
    }
}

fun WeatherDto.mapToWeatherInfo(): WeatherInfo {
    val mappedWeatherData = weatherData.mapToWeatherData()
    val currentTime = LocalDateTime.now()
    val currentWeatherData = mappedWeatherData[0]?.find { data ->
        val hour = if (currentTime.minute < 30) {
            currentTime.hour
        } else currentTime.hour + 1
        data.time.hour == hour
    }
    return WeatherInfo(
        weatherDataPerDay = mappedWeatherData,
        currentWeatherData = currentWeatherData
    )
}
