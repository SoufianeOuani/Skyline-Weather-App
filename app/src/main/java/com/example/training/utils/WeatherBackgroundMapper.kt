package com.example.training.utils

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun getWeatherBackground(condition: String, isDay: Int): List<Color> {

    val isNight = isDay == 0
    val c = condition.lowercase()

    return when {

        c.contains("sunny") || c.contains("clear") -> {
            if (isNight) listOf(
                Color(0xFF0F2027),
                Color(0xFF203A43),
                Color(0xFF2C5364)
            ) else listOf(
                Color(0xFF56CCF2),
                Color(0xFF2F80ED)
            )
        }

        c.contains("cloud") -> listOf(
            Color(0xFF757F9A),
            Color(0xFFD7DDE8)
        )

        c.contains("rain") -> listOf(
            Color(0xFF314755),
            Color(0xFF26A0DA)
        )

        c.contains("snow") -> listOf(
            Color(0xFFE6DADA),
            Color(0xFF274046)
        )

        else -> listOf(
            Color(0xFF6A5AE0),
            Color(0xFF9181F4)
        )
    }
}