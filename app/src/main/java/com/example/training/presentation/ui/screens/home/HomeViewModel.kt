package com.example.training.presentation.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.training.data.remote.ApiClient
import com.example.training.data.remote.dto.FavoriteCity
import com.example.training.data.remote.dto.Hour
import com.example.training.data.remote.dto.SearchResponse
import com.example.training.domain.model.ForecastDay
import com.example.training.domain.model.HourlyData
import com.example.training.utils.formatDay
import com.example.training.utils.getCurrentDayName
import com.example.training.utils.getWeatherBackground
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    private val apiKey = "dfdcd2ed7ff6453b998212947261804"

    private var searchJob: Job? = null

    init {
        getWeather("Rabat")
    }

    // =========================================================
    // 🔍 SEARCH
    // =========================================================
    fun onSearchQueryChange(query: String) {

        state = state.copy(searchQuery = query)

        searchJob?.cancel()

        if (query.length < 2) {
            state = state.copy(suggestions = emptyList())
            return
        }

        searchJob = viewModelScope.launch {
            delay(400)

            try {
                val results = ApiClient.api.searchCity(apiKey, query)
                state = state.copy(suggestions = results)
            } catch (_: Exception) {}
        }
    }

    fun onCitySelected(city: SearchResponse) {
        state = state.copy(
            searchQuery = city.name,
            suggestions = emptyList()
        )
        getWeather(city.name)
    }

    // =========================================================
    // ⭐ FAVORITES
    // =========================================================
    fun addToFavorites() {
        if (state.favorites.any { it.name == state.city }) return

        val favorite = FavoriteCity(
            name = state.city,
            temp = state.temperature,
            iconUrl = state.iconUrl
        )

        state = state.copy(
            favorites = state.favorites + favorite
        )
    }

    fun removeFromFavorites(city: String) {
        state = state.copy(
            favorites = state.favorites.filterNot { it.name == city }
        )
    }

    fun onFavoriteClicked(city: String) {
        getWeather(city)
    }

    // =========================================================
    // 🌤️ WEATHER
    // =========================================================
    private fun getWeather(city: String) {

        viewModelScope.launch {
            try {

                // =========================
                // CURRENT WEATHER
                // =========================
                val currentResponse = ApiClient.api.getCurrentWeather(
                    apiKey = apiKey,
                    city = city
                )

                val conditionText = currentResponse.current.condition.text
                val isDay = currentResponse.current.is_day

                val background = getWeatherBackground(conditionText, isDay)

                val localTime = currentResponse.location.localtime
                val formattedTime = localTime.substringAfter(" ")

                // =========================
                // FORECAST
                // =========================
                val forecastResponse = ApiClient.api.getForecast(
                    apiKey = apiKey,
                    city = city,
                    days = 7
                )

                val forecastDays = forecastResponse.forecast.forecastday
                val today = forecastDays.firstOrNull()

                // =========================
                // WEEK FORECAST
                // =========================
                val weekForecast = forecastDays.map { dayDto ->
                    ForecastDay(
                        day = formatDay(dayDto.date),
                        iconUrl = "https:${dayDto.day.condition.icon}",
                        maxTemp = "${dayDto.day.maxtemp_c.toInt()}°",
                        minTemp = "${dayDto.day.mintemp_c.toInt()}°",
                        windSpeed = "${dayDto.day.maxwind_kph.toInt()} km/h"
                    )
                }

                // =========================
                // ⏰ HOURLY FORECAST (TODAY)
                // =========================
                val hourlyForecast = forecastDays.firstOrNull()?.hour?.let {
                    mapHourly(it)
                } ?: emptyList()

                // =========================
                // STATE UPDATE
                // =========================
                state = state.copy(
                    temperature = "${currentResponse.current.temp_c.toInt()}°",
                    city = currentResponse.location.name,
                    condition = conditionText,
                    day = getCurrentDayName(),
                    time = formattedTime,

                    iconUrl = "https:${currentResponse.current.condition.icon}",
                    backgroundColors = background,

                    maxTemp = "${today?.day?.maxtemp_c?.toInt() ?: 0}°",
                    minTemp = "${today?.day?.mintemp_c?.toInt() ?: 0}°",

                    weekForecast = weekForecast,
                    hourlyForecast = hourlyForecast // ✅ NEW
                )

            } catch (e: Exception) {
                state = state.copy(error = "Error loading weather")
            }
        }
    }

    // =========================================================
    // 🔄 HOURLY MAPPER
    // =========================================================
    private fun mapHourly(hourList: List<Hour>): List<HourlyData> {
        return hourList.map {

            val time = it.time.substringAfter(" ") // 2026-04-24 15:00 → 15:00

            HourlyData(
                time = time,
                temp = "${it.temp_c.toInt()}°",
                iconUrl = "https:${it.condition.icon}"
            )
        }
    }
}