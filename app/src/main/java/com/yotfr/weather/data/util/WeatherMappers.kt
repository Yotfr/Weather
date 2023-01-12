package com.yotfr.weather.data.util

import com.yotfr.weather.data.datasource.remote.WeatherDataDto
import com.yotfr.weather.data.datasource.remote.WeatherDto
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
        val parsedTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME)
        IndexedWeatherData(
            index = index,
            data = WeatherData(
                time = parsedTime,
                temperature = temperature,
                pressure = pressure,
                windSpeed = windSpeed,
                humidity = humidity,
                weatherType = WeatherType.fromWMO(
                    code = weatherCode,
                    isDayTime = parsedTime.hour in 7..20
                )
            )
        )
    }.groupBy {
        it.index / 24
    }.mapValues { values ->
        values.value.map { it.data }
    }
}

fun WeatherDto.mapToWeatherInfo(): WeatherInfo {
    val detailedMappedWeatherData = weatherData.mapToWeatherData()
    val currentTime = LocalDateTime.now()
    val currentWeatherData = detailedMappedWeatherData[0]?.find { data ->
        val hour = if (currentTime.minute < 30) {
            currentTime.hour
        } else currentTime.hour + 1
        data.time.hour == hour
    }
    return WeatherInfo(
        detailedWeatherDataPerDay = detailedMappedWeatherData,
        currentWeatherData = currentWeatherData
    )
}
