package com.example.skyline.utils


object WeatherTranslator {

    fun translate(condition: String, lang: String): String {

        return when (condition.lowercase()) {

            "sunny" -> when (lang) {
                "fr" -> "Ensoleillé"
                "ar" -> "مشمس"
                "es" -> "Soleado"
                else -> "Sunny"
            }

            "partly cloudy" -> when (lang) {
                "fr" -> "Partiellement nuageux"
                "ar" -> "غائم جزئياً"
                "es" -> "Parcialmente nublado"
                else -> "Partly cloudy"
            }

            "rain" -> when (lang) {
                "fr" -> "Pluie"
                "ar" -> "مطر"
                "es" -> "Lluvia"
                else -> "Rain"
            }

            else -> condition
        }
    }
}