package com.example.skyline.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorites")
data class FavoriteCityEntity(

    @PrimaryKey
    val name: String,

    val temp: String,
    val iconUrl: String
)