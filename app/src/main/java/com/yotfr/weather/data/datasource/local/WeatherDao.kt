package com.yotfr.weather.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherData(weatherData: WeatherDataEntity)

    @Query("DELETE FROM weatherdata")
    suspend fun deleteWeatherData()

    @Query("SELECT * FROM weatherdata")
    suspend fun getWeatherData(): WeatherDataEntity?
}
