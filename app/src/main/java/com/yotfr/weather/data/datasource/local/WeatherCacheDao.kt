package com.yotfr.weather.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yotfr.weather.data.datasource.local.entities.DailyWeatherCacheEntity
import com.yotfr.weather.data.datasource.local.entities.HourlyWeatherCacheEntity

@Dao
interface WeatherCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyWeatherCache(weatherData: HourlyWeatherCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyWeatherCache(weatherData: DailyWeatherCacheEntity)

    @Query("DELETE FROM hourly")
    suspend fun deleteHourlyWeatherCache()

    @Query("DELETE FROM daily")
    suspend fun deleteDailyWeatherCache()

    @Query("SELECT * FROM hourly")
    suspend fun getHourlyWeatherCache(): List<HourlyWeatherCacheEntity>

    @Query("SELECT * FROM daily")
    suspend fun getDailyWeatherCache(): List<DailyWeatherCacheEntity>
}
