package com.example.training.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector

fun getWeatherIcon(condition: String, isDay: Int): ImageVector {

    val isNight = isDay == 0
    val c = condition.lowercase()

    return when {

        c.contains("sunny") || c.contains("clear") -> {
            if (isNight) Icons.Default.NightsStay
            else Icons.Default.WbSunny
        }

        c.contains("cloud") -> Icons.Default.Cloud

        c.contains("rain") || c.contains("drizzle") -> Icons.Default.WbCloudy

        c.contains("snow") -> Icons.Default.AcUnit

        c.contains("mist") || c.contains("fog") -> Icons.Default.WbCloudy

        else -> Icons.Default.WbSunny
    }
}