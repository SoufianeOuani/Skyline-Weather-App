package com.example.skyline.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: WeatherDatabase? = null

    fun getDatabase(context: Context): WeatherDatabase {
        return INSTANCE ?: synchronized(this) {

            val instance = Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java,
                "skyline_database"
            )
                .fallbackToDestructiveMigration() // 🔥 safe for dev
                .build()

            INSTANCE = instance
            instance
        }
    }
}