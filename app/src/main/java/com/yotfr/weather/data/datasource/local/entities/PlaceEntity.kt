package com.yotfr.weather.data.datasource.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "place")
data class PlaceEntity(
    @PrimaryKey val id: Long,
    val placeName: String,
    val latitude: Double,
    val longitude: Double,
    val countryName: String,
    val timeZone: String
)