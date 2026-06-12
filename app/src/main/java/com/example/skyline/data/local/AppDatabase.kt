package com.example.skyline.data.local


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteCityEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao
}