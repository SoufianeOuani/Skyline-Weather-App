package com.example.skyline.data.local


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorites")
    fun getFavorites(): Flow<List<FavoriteCityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(city: FavoriteCityEntity)

    @Delete
    suspend fun deleteFavorite(city: FavoriteCityEntity)

    @Query("DELETE FROM favorites WHERE name = :cityName")
    suspend fun deleteByName(cityName: String)
}