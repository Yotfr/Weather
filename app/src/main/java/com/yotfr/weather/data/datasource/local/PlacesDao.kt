package com.yotfr.weather.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.yotfr.weather.data.datasource.local.entities.FavoritePlaceEntity
import com.yotfr.weather.data.datasource.local.relation.PlaceWithWeatherCache
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoritePlace(favoritePlaceEntity: FavoritePlaceEntity): Long

    @Query("SELECT * FROM place")
    fun getAllFavoritePlaces(): Flow<List<PlaceWithWeatherCache>>

    @Transaction
    @Query("SELECT * FROM place WHERE id = :placeId")
    suspend fun getFavoritePlaceByPlaceId(placeId: Long): PlaceWithWeatherCache
}