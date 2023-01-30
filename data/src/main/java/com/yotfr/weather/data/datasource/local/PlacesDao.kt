package com.yotfr.weather.data.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.yotfr.weather.data.datasource.local.entities.FavoritePlaceEntity
import com.yotfr.weather.data.datasource.local.relations.PlaceWithWeatherCache

@Dao
interface PlacesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritePlace(favoritePlaceEntity: FavoritePlaceEntity): Long

    @Query("SELECT * FROM place")
    fun getAllFavoritePlaces(): List<PlaceWithWeatherCache>

    @Transaction
    @Query("SELECT * FROM place WHERE id = :placeId")
    suspend fun getFavoritePlaceById(placeId: Long): PlaceWithWeatherCache

    @Delete
    suspend fun deleteFavoritePlace(place: FavoritePlaceEntity)

    @Query("SELECT EXISTS(SELECT * FROM place WHERE id = :placeId)")
    suspend fun isFavoritePlaceExists(placeId: Long): Boolean
}
