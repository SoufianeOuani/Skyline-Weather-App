package com.example.skyline.utils

import com.example.skyline.R

fun mapConditionToStringRes(condition: String): Int {

    val text = condition.lowercase()

    return when {

        text.contains("sunny") -> R.string.weather_sunny
        text.contains("clear") -> R.string.weather_clear
        text.contains("partly cloudy") -> R.string.weather_partly_cloudy
        text.contains("cloudy") -> R.string.weather_cloudy
        text.contains("overcast") -> R.string.weather_overcast

        text.contains("mist") || text.contains("fog") ->
            R.string.weather_fog

        text.contains("rain") && text.contains("light") ->
            R.string.weather_light_rain

        text.contains("rain") ->
            R.string.weather_rain

        text.contains("storm") ->
            R.string.weather_storm

        text.contains("snow") ->
            R.string.weather_snow

        else -> R.string.weather_unknown
    }
}