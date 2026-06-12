package com.example.skyline.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LanguageManager {

    // 🔥 Apply language to context
    fun setLocale(context: Context, lang: String): Context {

        LanguageState.currentLanguage.value = lang // 🔥 triggers UI update

        val locale = Locale(lang)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }
    // 🌍 Auto detect device language
    fun getDeviceLanguage(): String {
        val locale = Locale.getDefault()

        return when {
            locale.language == "ar" -> "ar"
            locale.language == "fr" -> "fr"
            locale.country == "MA" -> "fr" // 🇲🇦 Morocco default
            locale.language == "es" -> "es"
            else -> "en"
        }
    }
}