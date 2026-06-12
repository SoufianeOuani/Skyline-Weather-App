package com.example.skyline.utils

import androidx.compose.ui.graphics.Color

//fun getWeatherBackground(condition: String, isDay: Int): List<Color> {
//
//    val isNight = isDay == 0
//    val c = condition.lowercase()
//
//    return when {
//
//        c.contains("sunny") || c.contains("clear") -> {
//            if (isNight) listOf(
//                Color(0xFF0F2027),
//                Color(0xFF203A43),
//                Color(0xFF2C5364)
//            ) else listOf(
//                Color(0xFF56CCF2),
//                Color(0xFF2F80ED)
//            )
//        }
//
//        c.contains("cloud") -> listOf(
//            Color(0xFF757F9A),
//            Color(0xFFD7DDE8)
//        )
//
//        c.contains("rain") -> listOf(
//            Color(0xFF314755),
//            Color(0xFF26A0DA)
//        )
//
//        c.contains("snow") -> listOf(
//            Color(0xFFE6DADA),
//            Color(0xFF274046)
//        )
//
//        else -> listOf(
//            Color(0xFF6A5AE0),
//            Color(0xFF9181F4)
//        )
//    }
//}


//fun getWeatherBackground(condition: String, isDay: Int): List<Color> {
//
//    val c = condition.lowercase()
//
//    return when {
//
//        c.contains("sunny") || c.contains("clear") ->
//            if (isDay == 1)
//                listOf(Color(0xFF4FACFE), Color(0xFF00F2FE))
//            else
//                listOf(Color(0xFF0F2027), Color(0xFF203A43))
//
//        c.contains("cloud") ->
//            listOf(Color(0xFF5A6FD8), Color(0xFF3A4DAF))
//
//        c.contains("rain") ->
//            listOf(Color(0xFF2C3E50), Color(0xFF4CA1AF))
//
//        c.contains("storm") || c.contains("thunder") ->
//            listOf(Color(0xFF141E30), Color(0xFF243B55))
//
//        c.contains("snow") ->
//            listOf(Color(0xFFE6E9F0), Color(0xFFEEF1F5))
//
//        else ->
//            listOf(Color(0xFF4FACFE), Color(0xFF00F2FE))
//    }
//}

fun getWeatherBackground(condition: String, isDay: Int): List<Color> {
    val isNight = isDay == 0
    val c = condition.lowercase()

    return when {
        // --- CLEAR / SUNNY ---
        c.contains("sunny") || c.contains("clear") -> {
            if (isNight) listOf(
                Color(0xFF0F172A), // Deep Space
                Color(0xFF1E1B4B), // Cosmic Indigo
                Color(0xFF020617)  // Near Black
            ) else listOf(
                Color(0xFF38BDF8), // Sky Blue
                Color(0xFF0284C7), // Deep Azure
                Color(0xFF0C4A6E)  // Dark Ocean
            )
        }

        // --- CLOUDY / OVERCAST ---
        c.contains("cloud") || c.contains("overcast") -> {
            if (isNight) listOf(
                Color(0xFF1E293B), // Slate Grey
                Color(0xFF0F172A), // Dark Navy
                Color(0xFF020617)
            ) else listOf(
                Color(0xFF94A3B8), // Cool Silver
                Color(0xFF475569), // Steel Blue
                Color(0xFF1E293B)  // Charcoal
            )
        }

        // --- RAIN / DRIZZLE ---
        c.contains("rain") || c.contains("drizzle") || c.contains("patchy") -> {
            listOf(
                Color(0xFF475569), // Storm Grey
                Color(0xFF1E3A8A), // Rainy Navy
                Color(0xFF0F172A)  // Deep Blue
            )
        }

        // --- THUNDERSTORM ---
        c.contains("thunder") || c.contains("storm") -> {
            listOf(
                Color(0xFF2E1065), // Deep Violet
                Color(0xFF1E1B4B), // Indigo Storm
                Color(0xFF020617)  // Void
            )
        }

        // --- SNOW / ICE ---
        c.contains("snow") || c.contains("ice") || c.contains("blizzard") -> {
            listOf(
                Color(0xFFE2E8F0), // Arctic White
                Color(0xFF94A3B8), // Frost Blue
                Color(0xFF475569)  // Shadow Slate
            )
        }

        // --- MIST / FOG / HAZY ---
        c.contains("mist") || c.contains("fog") || c.contains("haze") -> {
            listOf(
                Color(0xFF64748B), // Haze Grey
                Color(0xFF334155), // Muted Navy
                Color(0xFF0F172A)
            )
        }

        // --- DEFAULT / UNKNOWN ---
        else -> listOf(
            Color(0xFF1E40AF), // Brand Blue
            Color(0xFF1E3A8A),
            Color(0xFF172554)
        )
    }
}