package com.yotfr.weather.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yotfr.weather.data.datasource.local.entities.PlaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlace(placeEntity: PlaceEntity)

    @Query("SELECT * FROM place")
    fun getAllPlaces(): Flow<List<PlaceEntity>>
}