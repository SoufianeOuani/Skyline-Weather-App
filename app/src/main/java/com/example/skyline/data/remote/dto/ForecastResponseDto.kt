package com.example.skyline.data.remote.dto

data class ForecastResponse(
    val forecast: Forecast
)

data class Forecast(
    val forecastday: List<ForecastDayDto>
)

data class ForecastDayDto(
    val date: String,
    val day: Day,
    val hour: List<Hour>
)

data class Day(
    val maxtemp_c: Double,
    val mintemp_c: Double,
    val maxwind_kph: Double, // ✅ REQUIRED
    val condition: Condition,
    val daily_chance_of_rain: Int
)

data class Hour(
    val time: String,
    val temp_c: Double,
    val wind_kph: Double,
    val condition: Condition,
    val precip_mm: Float,
    val will_it_rain: Int
)