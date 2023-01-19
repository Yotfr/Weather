package com.yotfr.weather.data.util

import com.yotfr.weather.data.datasource.local.entities.CacheData
import com.yotfr.weather.data.datasource.local.entities.DailyWeatherCacheEntity
import com.yotfr.weather.data.datasource.local.entities.HourlyWeatherCacheEntity
import com.yotfr.weather.data.datasource.remote.dto.WeatherDto
import com.yotfr.weather.domain.model.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @param[index] is an hour of week
 * @param[index] is a weather information for hour with [index]
 */
private data class IndexedWeatherData(
    val index: Int,
    val data: HourlyWeatherData
)

private data class IndexedDailyWeatherData(
    val index: Int,
    val data: DailyWeatherData
)

fun WeatherDto.mapToWeatherInfo(): WeatherInfo {
    val indexedHourlyWeatherData = hourlyWeatherData.time.mapIndexed { index, time ->

        val temperature = hourlyWeatherData.temperatures[index]
        val weatherCode = hourlyWeatherData.weatherCodes[index]
        val windSpeed = hourlyWeatherData.windSpeeds[index]
        val pressure = hourlyWeatherData.pressures[index]
        val humidity = hourlyWeatherData.humidities[index]
        val apparentTemperature = hourlyWeatherData.apparentTemperature[index]
        val parsedTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME)
        val sunriseTime = dailyWeatherData.sunrise[index/24]
        val parsedSunriseTime = LocalDateTime.parse(sunriseTime, DateTimeFormatter.ISO_DATE_TIME)
        val sunsetTime = dailyWeatherData.sunset[index/24]
        val parsedSunsetTime = LocalDateTime.parse(sunsetTime, DateTimeFormatter.ISO_DATE_TIME)
        val isDayTime = parsedTime in parsedSunriseTime..parsedSunsetTime

        IndexedWeatherData(
            index = index,
            data = HourlyWeatherData(
                time = parsedTime,
                temperature = temperature,
                pressure = pressure,
                windSpeed = windSpeed,
                humidity = humidity,
                weatherType = WeatherType.fromWMO(
                    code = weatherCode,
                    isDayTime = isDayTime
                ),
                apparentTemperature = apparentTemperature
            )
        )
    }

    val mappedHourlyWeatherData = indexedHourlyWeatherData.groupBy {
        it.index / 24
    }.mapValues { values ->
        values.value.map { it.data }
    }

    val mappedDailyWeatherData = dailyWeatherData.time.mapIndexed { index, time ->

        val parsedTime = LocalDate.parse(time, DateTimeFormatter.ISO_DATE)
        val weatherCode = dailyWeatherData.weatherCodes[index]
        val maxTemperature = dailyWeatherData.maxTemperature[index]
        val minTemperature = dailyWeatherData.minTemperature[index]
        val sunriseTime = dailyWeatherData.sunrise[index]
        val parsedSunriseTime = LocalDateTime.parse(sunriseTime, DateTimeFormatter.ISO_DATE_TIME)
        val sunsetTime = dailyWeatherData.sunset[index]
        val parsedSunSetTime = LocalDateTime.parse(sunsetTime, DateTimeFormatter.ISO_DATE_TIME)

        IndexedDailyWeatherData(
            index = index,
            data = DailyWeatherData(
                time = parsedTime,
                weatherType = WeatherType.fromWMO(
                    code = weatherCode,
                    isDayTime = true
                ),
                maxTemperature = maxTemperature,
                minTemperature = minTemperature,
                sunrise = parsedSunriseTime,
                sunset = parsedSunSetTime
            )
        )
    }.groupBy {
        it.index
    }.mapValues { values ->
        values.value[0].data
    }

    val currentTime = LocalDateTime.now()
    val currentWeatherData = mappedHourlyWeatherData[0]?.find { data ->
        val hour = if (currentTime.minute < 30) {
            currentTime.hour
        } else currentTime.hour + 1
        data.time.hour == hour
    } ?: throw Exception(
        "cannot find value for this key"
    )

    val initialIndex = indexedHourlyWeatherData.indexOfFirst { data ->
        val hour = if (currentTime.minute < 30) {
            currentTime.hour
        } else currentTime.hour + 1
        data.data.time.hour == hour
    }

    val fromCurrentTimeHourlyWeatherData = indexedHourlyWeatherData.subList(
        fromIndex = initialIndex,
        toIndex = initialIndex + 23
    ).map {
        it.data
    }

    val todaySunrise = mappedDailyWeatherData[0]?.sunrise ?: throw Exception(
        "cannot find value for this key"
    )
    val todaySunset = mappedDailyWeatherData[0]?.sunset ?: throw Exception(
        "cannot find value for this key"
    )

    val completeWeatherData = mappedDailyWeatherData.mapValues { values ->
        CompleteWeatherData(
            hourlyWeatherData = mappedHourlyWeatherData[values.key] ?: throw Exception(
                "cannot find value for this key"
            ),
            dailyWeatherData = values.value
        )
    }

    return WeatherInfo(
        completeWeatherData = completeWeatherData,
        currentWeatherData = currentWeatherData,
        fromCurrentTimeHourlyWeatherData = fromCurrentTimeHourlyWeatherData,
        todaySunrise = todaySunrise,
        todaySunset = todaySunset
    )
}

fun CacheData.mapToWeatherInfo(): WeatherInfo {
    val indexedHourlyWeatherData = hourlyWeatherCacheEntity.time.mapIndexed { index, time ->

        val temperature = hourlyWeatherCacheEntity.temperatures[index]
        val weatherCode = hourlyWeatherCacheEntity.weatherCodes[index]
        val windSpeed = hourlyWeatherCacheEntity.windSpeeds[index]
        val pressure = hourlyWeatherCacheEntity.pressures[index]
        val humidity = hourlyWeatherCacheEntity.humidities[index]
        val apparentTemperature = hourlyWeatherCacheEntity.apparentTemperature[index]
        val parsedTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME)
        val sunriseTime = dailyWeatherCacheEntity.sunrise[index/24]
        val parsedSunriseTime = LocalDateTime.parse(sunriseTime, DateTimeFormatter.ISO_DATE_TIME)
        val sunsetTime = dailyWeatherCacheEntity.sunset[index/24]
        val parsedSunsetTime = LocalDateTime.parse(sunsetTime, DateTimeFormatter.ISO_DATE_TIME)
        val isDayTime = parsedTime in parsedSunriseTime..parsedSunsetTime

        IndexedWeatherData(
            index = index,
            data = HourlyWeatherData(
                time = parsedTime,
                temperature = temperature,
                pressure = pressure,
                windSpeed = windSpeed,
                humidity = humidity,
                weatherType = WeatherType.fromWMO(
                    code = weatherCode,
                    isDayTime = isDayTime
                ),
                apparentTemperature = apparentTemperature
            )
        )
    }

    val mappedHourlyWeatherData = indexedHourlyWeatherData.groupBy {
        it.index / 24
    }.mapValues { values ->
        values.value.map { it.data }
    }



    val mappedDailyWeatherData = dailyWeatherCacheEntity.time.mapIndexed { index, time ->

        val parsedTime = LocalDate.parse(time, DateTimeFormatter.ISO_DATE)
        val weatherCode = dailyWeatherCacheEntity.weatherCodes[index]
        val maxTemperature = dailyWeatherCacheEntity.maxTemperature[index]
        val minTemperature = dailyWeatherCacheEntity.minTemperature[index]
        val sunriseTime = dailyWeatherCacheEntity.sunrise[index]
        val parsedSunriseTime = LocalDateTime.parse(sunriseTime, DateTimeFormatter.ISO_DATE_TIME)
        val sunsetTime = dailyWeatherCacheEntity.sunset[index]
        val parsedSunSetTime = LocalDateTime.parse(sunsetTime, DateTimeFormatter.ISO_DATE_TIME)

        IndexedDailyWeatherData(
            index = index,
            data = DailyWeatherData(
                time = parsedTime,
                weatherType = WeatherType.fromWMO(
                    code = weatherCode,
                    isDayTime = true
                ),
                maxTemperature = maxTemperature,
                minTemperature = minTemperature,
                sunrise = parsedSunriseTime,
                sunset = parsedSunSetTime
            )
        )
    }.groupBy {
        it.index
    }.mapValues { values ->
        values.value[0].data
    }

    val currentTime = LocalDateTime.now()
    val currentWeatherData = mappedHourlyWeatherData[0]?.find { data ->
        val hour = if (currentTime.minute < 30) {
            currentTime.hour
        } else currentTime.hour + 1
        data.time.hour == hour
    } ?: throw Exception(
        "cannot find value for this key"
    )

    val initialIndex = indexedHourlyWeatherData.indexOfFirst { data ->
        val hour = if (currentTime.minute < 30) {
            currentTime.hour
        } else currentTime.hour + 1
        data.data.time.hour == hour
    }

    val fromCurrentTimeHourlyWeatherData = indexedHourlyWeatherData.subList(
        fromIndex = initialIndex,
        toIndex = initialIndex + 23
    ).map {
        it.data
    }

    val todaySunrise = mappedDailyWeatherData[0]?.sunrise ?: throw Exception(
        "cannot find value for this key"
    )
    val todaySunset = mappedDailyWeatherData[0]?.sunset ?: throw Exception(
        "cannot find value for this key"
    )

    val completeWeatherData = mappedDailyWeatherData.mapValues { values ->
        CompleteWeatherData(
            hourlyWeatherData = mappedHourlyWeatherData[values.key] ?: throw Exception(
                "cannot find value for this key"
            ),
            dailyWeatherData = values.value
        )
    }

    return WeatherInfo(
        completeWeatherData = completeWeatherData,
        currentWeatherData = currentWeatherData,
        fromCurrentTimeHourlyWeatherData = fromCurrentTimeHourlyWeatherData,
        todaySunrise = todaySunrise,
        todaySunset = todaySunset
    )
}

/**
 * Api returns weather information for hours of week without dividing into days, so we need to
 * divide index by 24
 */

fun WeatherDto.mapToHourlyCache(): HourlyWeatherCacheEntity {
    return HourlyWeatherCacheEntity(
        time = hourlyWeatherData.time,
        temperatures = hourlyWeatherData.temperatures,
        weatherCodes = hourlyWeatherData.weatherCodes,
        pressures = hourlyWeatherData.pressures,
        windSpeeds = hourlyWeatherData.windSpeeds,
        humidities = hourlyWeatherData.humidities,
        apparentTemperature = hourlyWeatherData.apparentTemperature,
        id = 0
    )
}

fun WeatherDto.mapToDailyCache(): DailyWeatherCacheEntity {
    return DailyWeatherCacheEntity(
        time = dailyWeatherData.time,
        weatherCodes = dailyWeatherData.weatherCodes,
        maxTemperature = dailyWeatherData.maxTemperature,
        minTemperature = dailyWeatherData.minTemperature,
        sunrise = dailyWeatherData.sunrise,
        sunset = dailyWeatherData.sunset,
        id = 0
    )
}
