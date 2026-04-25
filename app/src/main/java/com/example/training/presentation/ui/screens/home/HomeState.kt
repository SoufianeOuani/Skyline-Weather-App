package com.example.training.presentation.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.training.data.remote.dto.FavoriteCity
import com.example.training.data.remote.dto.SearchResponse
import com.example.training.domain.model.CityForecast
import com.example.training.domain.model.ForecastDay
import com.example.training.domain.model.HourlyData

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
    val error: String = "",
    val searchQuery: String = "",
    val suggestions: List<SearchResponse> = emptyList(),
    val favorites: List<FavoriteCity> = emptyList(),
    val time: String = "",
    val hourlyForecast: List<HourlyData> = emptyList()
)