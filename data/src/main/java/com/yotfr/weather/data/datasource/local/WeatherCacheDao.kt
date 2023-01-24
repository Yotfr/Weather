package com.yotfr.weather.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yotfr.weather.data.datasource.local.entities.WeatherCacheEntity

@Dao
interface WeatherCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherCache(weatherData: WeatherCacheEntity)

    @Query("DELETE FROM weathercache WHERE placeId = :placeId")
    suspend fun deleteWeatherCache(placeId: Long)
}
