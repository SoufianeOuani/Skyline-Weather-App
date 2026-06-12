package com.example.skyline.presentation.ui.screens.home

import androidx.compose.ui.graphics.Color
import com.example.skyline.data.remote.dto.FavoriteCity
import com.example.skyline.data.remote.dto.SearchResponse
import com.example.skyline.domain.model.CityForecast
import com.example.skyline.domain.model.ForecastDay
import com.example.skyline.domain.model.HourlyData
import com.example.skyline.domain.model.WeatherMode

data class HomeState(
    val temperature: String = "",
    val day: String = "",
    val city: String = "",
    val condition: String = "",

    val iconUrl : String = "",

    val backgroundColors: List<Color> = listOf(
        Color(0xFF6A5AE0),
        Color(0xFF9181F4)
    ),

    val maxTemp: String = "",
    val minTemp: String = "",

    val weekForecast: List<ForecastDay> = emptyList(),
    val cityForecast: List<CityForecast> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = "",
    val searchQuery: String = "",
    val suggestions: List<SearchResponse> = emptyList(),
    val favorites: List<FavoriteCity> = emptyList(),
    val time: String = "",
    val hourlyForecast: List<HourlyData> = emptyList(),
    val selectedMode: WeatherMode = WeatherMode.TEMP,
    val currentHour: Int = 0,
    val humidity: String = "",
    val windSpeed: String = "",
    val uvIndex: String = "",
    val rainChance: Int = 0,
    val feelsLike: String = "",
    val pressure: String = "",
    val isMyLocation: Boolean = false,
    val isRefreshing: Boolean = false,

    val currentTime: String = "00:00",
    val currentDate: String = "",

    val currentLanguage: String = "en"
)