package com.example.skyline.domain.model

data class HourlyData(
    val time: String,
    val temp: String,
    val iconUrl: String,
    val wind: String,
    val rain: Float,
    val snow: Float
)