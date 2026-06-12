package com.example.skyline.data.remote.dto

data class WeatherResponse(
    val location: Location,
    val current: Current
)

data class Location(
    val name: String,
    val localtime: String
)

data class Current(
    val temp_c: Double,
    val is_day: Int,
    val condition: Condition,
    val feelslike_c: Double,
    val humidity: Int,
    val wind_kph: Double,
    val pressure_mb: Double,
    val uv: Double,
)


data class Condition(
    val text: String,
    val icon: String // 🔥 ADD THIS (important)
)