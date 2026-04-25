package com.example.training.data.remote.dto

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
    val condition: Condition
)

data class Hour(
    val time: String,
    val temp_c: Double,
    val condition: Condition
)