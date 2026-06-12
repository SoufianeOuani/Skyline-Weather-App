package com.example.skyline.domain.model

data class ForecastDay(
    val day: String,
    val iconUrl: String,
    val maxTemp: String,
    val minTemp: String,
    val windSpeed: String
)