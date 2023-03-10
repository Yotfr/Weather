package com.yotfr.weather.data.util

import com.yotfr.weather.data.datasource.local.entities.WeatherCacheEntity
import com.yotfr.weather.data.datasource.remote.dto.WeatherDto
import com.yotfr.weather.domain.model.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone.*

/**
 * [IndexedWeatherData] helper class to get access to the index outside of .mapIndexed
 */
private data class IndexedWeatherData(
    val index: Int,
    val data: HourlyWeatherData
)

/**
 * [IndexedDailyWeatherData] helper class to get access to the index outside of .mapIndexed
 */
private data class IndexedDailyWeatherData(
    val index: Int,
    val data: DailyWeatherData
)

fun WeatherDto.mapToWeatherInfo(timeZone: String): WeatherInfo {
    val indexedHourlyWeatherData = hourlyWeatherData.time.mapIndexed { index, time ->

        /*
         For each dateTime in the list of dateTimes we get weather information in that dateTime
         [sunriseTime] and [sunsetTime] divided by 24 because they refer to dailyWeatherData and
         [dailyWeatherData.sunrise || sunset] contains only 7 elements
         [isDayTime] required to determine day/night weatherTypes (e.g. ClearSkySun or ClearSkyMoon)
         */
        val temperature = hourlyWeatherData.temperatures[index]
        val weatherCode = hourlyWeatherData.weatherCodes[index]
        val windSpeed = hourlyWeatherData.windSpeeds[index]
        val pressure = hourlyWeatherData.pressures[index]
        val humidity = hourlyWeatherData.humidities[index]
        val apparentTemperature = hourlyWeatherData.apparentTemperature[index]
        val parsedTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME)
        val sunriseTime = dailyWeatherData.sunrise[index / 24]
        val parsedSunriseTime = LocalDateTime.parse(sunriseTime, DateTimeFormatter.ISO_DATE_TIME)
        val sunsetTime = dailyWeatherData.sunset[index / 24]
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

    // Group weather data by days, and get rid of indexes
    val mappedHourlyWeatherData = indexedHourlyWeatherData.groupBy {
        it.index / 24
    }.mapValues { values ->
        values.value.map { it.data }
    }

    /*
     For each date in the list of dates we get weather information in that date,
     group weather data by days, and get rid of indexes
     */
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
                date = parsedTime,
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

    // Get current zoned time
    val currentTime = LocalDateTime.now(
        getTimeZone(timeZone).toZoneId()
    )

    /*
     Get weather information of particular day
     */
    val currentWeatherData = mappedHourlyWeatherData[0]?.find { data ->
        val hour = if (currentTime.minute < 30) {
            currentTime.hour
        } else currentTime.hour + 1
        data.time.hour == hour
    } ?: throw Exception(
        "cannot find value for this key"
    )

    /*
    Get index of dateTime in dateTimes list that matches  current dateTime
     */
    val initialIndex = indexedHourlyWeatherData.indexOfFirst { data ->
        val hour = if (currentTime.minute < 30) {
            currentTime.hour
        } else currentTime.hour + 1
        data.data.time.hour == hour
    }

    /*
    Get list of hourly weather information for the next 24 hours (from current zoned time)
     */
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

fun WeatherCacheEntity.mapToWeatherInfo(timeZone: String): WeatherInfo {
    val indexedHourlyWeatherData = hourlyTime.mapIndexed { index, time ->

        val temperature = hourlyTemperatures[index]
        val weatherCode = hourlyWeatherCodes[index]
        val windSpeed = hourlyWindSpeeds[index]
        val pressure = hourlyPressures[index]
        val humidity = hourlyHumidities[index]
        val apparentTemperature = hourlyApparentTemperature[index]
        val parsedTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME)
        val sunriseTime = dailySunrise[index / 24]
        val parsedSunriseTime = LocalDateTime.parse(sunriseTime, DateTimeFormatter.ISO_DATE_TIME)
        val sunsetTime = dailySunset[index / 24]
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

    val mappedDailyWeatherData = dailyTime.mapIndexed { index, time ->

        val parsedTime = LocalDate.parse(time, DateTimeFormatter.ISO_DATE)
        val weatherCode = dailyWeatherCodes[index]
        val maxTemperature = dailyMaxTemperature[index]
        val minTemperature = dailyMinTemperature[index]
        val sunriseTime = dailySunrise[index]
        val parsedSunriseTime = LocalDateTime.parse(sunriseTime, DateTimeFormatter.ISO_DATE_TIME)
        val sunsetTime = dailySunset[index]
        val parsedSunSetTime = LocalDateTime.parse(sunsetTime, DateTimeFormatter.ISO_DATE_TIME)

        IndexedDailyWeatherData(
            index = index,
            data = DailyWeatherData(
                date = parsedTime,
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

    val currentTime = LocalDateTime.now(
        getTimeZone(timeZone).toZoneId()
    )
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

fun WeatherDto.mapToWeatherCacheEntity(
    placeId: Long
): WeatherCacheEntity {
    return WeatherCacheEntity(
        id = 0,
        dailyTime = dailyWeatherData.time,
        dailyWeatherCodes = dailyWeatherData.weatherCodes,
        dailyMaxTemperature = dailyWeatherData.maxTemperature,
        dailyMinTemperature = dailyWeatherData.minTemperature,
        dailySunrise = dailyWeatherData.sunrise,
        dailySunset = dailyWeatherData.sunset,
        hourlyTime = hourlyWeatherData.time,
        hourlyTemperatures = hourlyWeatherData.temperatures,
        hourlyWeatherCodes = hourlyWeatherData.weatherCodes,
        hourlyPressures = hourlyWeatherData.pressures,
        hourlyWindSpeeds = hourlyWeatherData.windSpeeds,
        hourlyHumidities = hourlyWeatherData.humidities,
        hourlyApparentTemperature = hourlyWeatherData.apparentTemperature,
        placeId = placeId
    )
}
