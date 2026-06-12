package com.example.skyline.presentation.ui.screens.home

import android.Manifest
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.skyline.data.local.DatabaseProvider
import com.example.skyline.data.local.FavoriteCityEntity
import com.example.skyline.data.remote.ApiClient
import com.example.skyline.data.remote.dto.FavoriteCity
import com.example.skyline.data.remote.dto.Hour
import com.example.skyline.data.remote.dto.SearchResponse
import com.example.skyline.domain.model.ForecastDay
import com.example.skyline.domain.model.HourlyData
import com.example.skyline.domain.model.WeatherMode
import com.example.skyline.utils.PreferenceHelper
import com.example.skyline.utils.getWeatherBackground
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest

//@RequiresApi(Build.VERSION_CODES.O)
//class HomeViewModel(application: Application) : AndroidViewModel(application) {
//
//    var state by mutableStateOf(HomeState())
//        private set
//
//    var favoriteCities by mutableStateOf<List<FavoriteCity>>(emptyList())
//        private set
//
//    private val apiKey = "dfdcd2ed7ff6453b998212947261804"
//
//    private var searchJob: Job? = null
//    private var autoRefreshJob: Job? = null
//    private var clockJob: Job? = null
//
//    private val dao = DatabaseProvider
//        .getDatabase(application)
//        .favoriteDao()
//
//    init {
//        updateLanguageState()
//        getUserLocationWeather()
//        startAutoRefresh()
//        startClock()
//        observeFavorites()
//    }
//
//
//    // =========================================================
//    // ⭐ FAVORITES
//    // =========================================================
//
//    private fun observeFavorites() {
//        viewModelScope.launch {
//            dao.getFavorites().collectLatest { list ->
//                favoriteCities = list.map {
//                    FavoriteCity(it.name, it.temp, it.iconUrl)
//                }
//            }
//        }
//    }
//
//    fun toggleFavorite(cityName: String? = null) {
//        val city = state.city
//        if (city.isBlank()) return
//
//        viewModelScope.launch {
//
//            val isFav = favoriteCities.any { it.name == city }
//
//            if (isFav) {
//                dao.deleteByName(city)
//            } else {
//                dao.insertFavorite(
//                    FavoriteCityEntity(
//                        name = city,
//                        temp = state.temperature,
//                        iconUrl = state.iconUrl
//                    )
//                )
//            }
//        }
//    }
//
//    fun isFavorite(): Boolean {
//        return favoriteCities.any { it.name == state.city }
//    }
//
//    // =========================================================
//    // 🌐 LANGUAGE
//    // =========================================================
//
//    private fun getCurrentLang(): String {
//        val prefs = getApplication<Application>()
//            .getSharedPreferences("settings", Context.MODE_PRIVATE)
//
//        return prefs.getString("lang", "en") ?: "en"
//    }
//
//    private fun updateLanguageState() {
//        val lang = getCurrentLang()
//        state = state.copy(currentLanguage = lang)
//    }
//
//    // =============================
//    // 🔄 RELOAD LOCATION
//    // =============================
//    fun reloadLocation() {
//        getUserLocationWeather()
//    }
//
//    // =============================
//    // 📍 GPS LOGIC (UPDATED)
//    // =============================
//    // 🔥 Restart event (ONE-TIME event)
//    private val _restartEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
//    val restartEvents = _restartEvents.asSharedFlow()
//
//    private fun getUserLocationWeather() {
//
//        val context = getApplication<Application>()
//
//        val fusedLocationClient =
//            com.google.android.gms.location.LocationServices
//                .getFusedLocationProviderClient(context)
//
//        try {
//
//            // ⚡ STEP 1: FAST LOCATION
//            fusedLocationClient.lastLocation
//                .addOnSuccessListener { location ->
//
//                    if (location != null) {
//
//                        val lat = location.latitude
//                        val lon = location.longitude
//
//                        val newLocation = "$lat,$lon"
//
//                        // ✅ avoid duplicate refresh
//                        if (state.isMyLocation && state.city == newLocation) return@addOnSuccessListener
//
//                        getWeather(newLocation, true)
//
//                        // 🔥 trigger restart dialog
//                        _restartEvents.tryEmit(Unit)
//
//                    } else {
//                        requestFreshLocation(fusedLocationClient)
//                    }
//                }
//
//                .addOnFailureListener {
//                    requestFreshLocation(fusedLocationClient)
//                }
//
//        } catch (e: SecurityException) {
//            fallbackToDefaultCity()
//        }
//    }
//
//    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
//    private fun requestFreshLocation(
//        fusedLocationClient: FusedLocationProviderClient
//    ) {
//
//        val cts = CancellationTokenSource()
//
//        fusedLocationClient.getCurrentLocation(
//            Priority.PRIORITY_HIGH_ACCURACY,
//            cts.token
//        ).addOnSuccessListener { location ->
//
//            if (location != null) {
//
//                val lat = location.latitude
//                val lon = location.longitude
//
//                val newLocation = "$lat,$lon"
//
//                getWeather(newLocation, true)
//
//                // 🔥 trigger restart dialog
//                _restartEvents.tryEmit(Unit)
//
//            } else {
//                fallbackToDefaultCity()
//            }
//
//        }.addOnFailureListener {
//            fallbackToDefaultCity()
//        }
//    }
//
//    private fun fallbackToDefaultCity() {
//        getWeather("Rabat", false)
//    }
//    // =========================================================
//    // 🔄 AUTO REFRESH
//    // =========================================================
//
//    private fun startAutoRefresh() {
//        autoRefreshJob?.cancel()
//
//        autoRefreshJob = viewModelScope.launch {
//            while (true) {
//                delay(5 * 60 * 1000L)
//
//                val city = state.city
//                if (city.isNotBlank()) {
//                    getWeather(city, state.isMyLocation)
//                }
//            }
//        }
//    }
//
//    // =========================================================
//    // ⏱ CLOCK
//    // =========================================================
//
//    private fun startClock() {
//        clockJob?.cancel()
//
//        clockJob = viewModelScope.launch {
//            while (true) {
//                delay(60_000L)
//
//                state = state.copy(
//                    currentHour = LocalTime.now().hour
//                )
//            }
//        }
//    }
//
//    // =========================================================
//    // 🔍 SEARCH
//    // =========================================================
//
//    fun onSearchQueryChange(query: String) {
//        state = state.copy(searchQuery = query)
//        searchJob?.cancel()
//
//        if (query.length < 2) {
//            state = state.copy(suggestions = emptyList())
//            return
//        }
//
//        searchJob = viewModelScope.launch {
//            delay(400)
//
//            try {
//                val results = ApiClient.api.searchCity(apiKey, query)
//                state = state.copy(suggestions = results)
//            } catch (_: Exception) {}
//        }
//    }
//
//    fun onCitySelected(city: SearchResponse) {
//        state = state.copy(
//            searchQuery = city.name,
//            suggestions = emptyList()
//        )
//        getWeather(city.name)
//    }
//
//    // =========================================================
//    // 🔄 REFRESH
//    // =========================================================
//
//    fun refreshWeather() {
//        updateLanguageState()
//
//        val city = state.city
//        if (city.isNotEmpty()) {
//            getWeather(city, state.isMyLocation)
//        }
//    }
//
//    fun loadWeather(city: String) {
//        if (city.isBlank()) return
//        if (state.city.equals(city, true)) return
//
//        PreferenceHelper.saveCity(getApplication(), city)
//        getWeather(city)
//    }
//
//    // =========================================================
//    // 🌤 WEATHER
//    // =========================================================
//
//    private fun getWeather(city: String, isMyLocation: Boolean = false) {
//
//
//        state = state.copy(isRefreshing = true, error = null)
//
//        viewModelScope.launch {
//
//            try {
//
//                val lang = getCurrentLang()
//                val locale = Locale(lang)
//
//                val currentResponse =
//                    ApiClient.api.getCurrentWeather(apiKey, city, lang)
//
//                val forecastResponse =
//                    ApiClient.api.getForecast(apiKey, city, 7, lang)
//
//                val conditionText = currentResponse.current.condition.text
//                val isDay = currentResponse.current.is_day
//
//                val background = getWeatherBackground(conditionText, isDay)
//
//                val localTime = currentResponse.location.localtime
//                val timePart = localTime.substringAfter(" ")
//
//                val currentHour = LocalTime.now().hour
//
//                val formatter = DateTimeFormatter.ofPattern("EEEE", locale)
//                val dayName = LocalDate.now().format(formatter)
//
//                val forecastDays = forecastResponse.forecast.forecastday
//                val today = forecastDays.firstOrNull()
//
//                val weekForecast = forecastDays.map {
//
//                    val localDate = LocalDate.parse(it.date)
//
//                    ForecastDay(
//                        day = localDate.format(formatter), // 🔥 FIXED
//                        iconUrl = "https:${it.day.condition.icon}",
//                        maxTemp = "${it.day.maxtemp_c.toInt()}°",
//                        minTemp = "${it.day.mintemp_c.toInt()}°",
//                        windSpeed = "${it.day.maxwind_kph.toInt()} km/h"
//                    )
//                }
//
//                val hourlyForecast = forecastDays.firstOrNull()?.hour?.let {
//                    mapHourly(it)
//                } ?: emptyList()
//
//                state = state.copy(
//                    temperature = "${currentResponse.current.temp_c.toInt()}°",
//                    city = currentResponse.location.name,
//                    condition = conditionText,
//                    day = dayName,
//                    time = timePart,
//                    iconUrl = "https:${currentResponse.current.condition.icon}",
//                    backgroundColors = background,
//
//                    maxTemp = "${today?.day?.maxtemp_c?.toInt() ?: 0}°",
//                    minTemp = "${today?.day?.mintemp_c?.toInt() ?: 0}°",
//
//                    weekForecast = weekForecast,
//                    hourlyForecast = hourlyForecast,
//                    currentHour = currentHour,
//
//                    humidity = "${currentResponse.current.humidity}%",
//                    windSpeed = "${currentResponse.current.wind_kph.toInt()} km/h",
//                    uvIndex = currentResponse.current.uv.toString(),
//                    rainChance = today?.day?.daily_chance_of_rain ?: 0,
//                    feelsLike = "${currentResponse.current.feelslike_c.toInt()}°",
//                    pressure = "${currentResponse.current.pressure_mb.toInt()} mb",
//
//                    isMyLocation = isMyLocation,
//                    isRefreshing = false,
//                    error = null
//                )
//
//        Log.d("FORECAST_SIZE", forecastResponse.forecast.forecastday.size.toString())
//            } catch (e: Exception) {
//                state = state.copy(
//                    error = "Error loading weather",
//                    isRefreshing = false
//                )
//            }
//        }
//    }
//
//    private fun mapHourly(hourList: List<Hour>): List<HourlyData> {
//        return hourList.map {
//            val time = it.time.substringAfter(" ")
//            val tempValue = it.temp_c.toFloat()
//            val precipitation = it.precip_mm
//
//            val snow = if (tempValue <= 0f) precipitation else 0f
//            val rain = if (tempValue > 0f) precipitation else 0f
//
//            HourlyData(
//                time = time,
//                temp = "${tempValue.toInt()}°",
//                iconUrl = "https:${it.condition.icon}",
//                wind = "${it.wind_kph.toInt()} km/h",
//                rain = rain,
//                snow = snow
//            )
//        }
//    }
//
//    fun changeMode(mode: WeatherMode) {
//        state = state.copy(selectedMode = mode)
//    }
//}


@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    var state by mutableStateOf(HomeState())
        private set

    var favoriteCities by mutableStateOf<List<FavoriteCity>>(emptyList())
        private set

    private val apiKey = "dfdcd2ed7ff6453b998212947261804"

    private var searchJob: Job? = null
    private var autoRefreshJob: Job? = null
    private var clockJob: Job? = null

    private val dao = DatabaseProvider
        .getDatabase(application)
        .favoriteDao()

    // 🔥 Restart event (Only triggered manually via triggerRestart)
    private val _restartEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val restartEvents = _restartEvents.asSharedFlow()

    init {
        updateLanguageState()
        getUserLocationWeather()
        startAutoRefresh()
        startClock()
        observeFavorites()
    }

    // =========================================================
    // ⭐ FAVORITES (Now includes specific removal)
    // =========================================================

    private fun observeFavorites() {
        viewModelScope.launch {
            dao.getFavorites().collectLatest { list ->
                favoriteCities = list.map {
                    FavoriteCity(it.name, it.temp, it.iconUrl)
                }
            }
        }
    }

    fun toggleFavorite(cityName: String? = null) {
        // Use provided cityName (from long-press) or the current state city
        val city = cityName ?: state.city
        if (city.isBlank()) return

        viewModelScope.launch {
            val isFav = favoriteCities.any { it.name.equals(city, ignoreCase = true) }

            if (isFav) {
                dao.deleteByName(city)
            } else {
                // If toggling the current city, use state data.
                // If it's an external city, we'd need to fetch its data (handled by clicking the card first usually)
                dao.insertFavorite(
                    FavoriteCityEntity(
                        name = city,
                        temp = state.temperature,
                        iconUrl = state.iconUrl
                    )
                )
            }
        }
    }

    fun isFavorite(): Boolean {
        return favoriteCities.any { it.name.equals(state.city, ignoreCase = true) }
    }

    // =========================================================
    // 🌐 LANGUAGE & RESTART
    // =========================================================

    private fun getCurrentLang(): String {
        val prefs = getApplication<Application>()
            .getSharedPreferences("settings", Context.MODE_PRIVATE)
        return prefs.getString("lang", "en") ?: "en"
    }

    private fun updateLanguageState() {
        val lang = getCurrentLang()
        state = state.copy(currentLanguage = lang)
    }

    /**
     * Call this from MainActivity when permission is granted
     * to trigger the restart logic in HomeScreen.
     */
    fun triggerRestart() {
        _restartEvents.tryEmit(Unit)
    }

    fun reloadLocation() {
        getUserLocationWeather()
    }

    // =========================================================
    // 📍 GPS LOGIC
    // =========================================================

    private fun getUserLocationWeather() {
        val context = getApplication<Application>()
        val fusedLocationClient = com.google.android.gms.location.LocationServices
            .getFusedLocationProviderClient(context)

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val newLocation = "${location.latitude},${location.longitude}"
                        getWeather(newLocation, true)
                    } else {
                        requestFreshLocation(fusedLocationClient)
                    }
                }
                .addOnFailureListener {
                    requestFreshLocation(fusedLocationClient)
                }
        } catch (e: SecurityException) {
            fallbackToDefaultCity()
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun requestFreshLocation(fusedLocationClient: FusedLocationProviderClient) {
        val cts = CancellationTokenSource()
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cts.token
        ).addOnSuccessListener { location ->
            if (location != null) {
                val newLocation = "${location.latitude},${location.longitude}"
                getWeather(newLocation, true)
            } else {
                fallbackToDefaultCity()
            }
        }.addOnFailureListener {
            fallbackToDefaultCity()
        }
    }

    private fun fallbackToDefaultCity() {
        // Only load Rabat if we don't already have a city loaded
        if (state.city.isBlank()) {
            getWeather("Rabat", false)
        }
    }

    // =========================================================
    // 🔍 SEARCH & REFRESH
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
            searchQuery = "", // Clear query on selection
            suggestions = emptyList()
        )
        getWeather(city.name)
    }

    fun refreshWeather() {
        updateLanguageState()
        val city = state.city
        if (city.isNotEmpty()) {
            getWeather(city, state.isMyLocation)
        }
    }

    fun loadWeather(city: String) {
        if (city.isBlank()) return
        // Allow reload if the user clicks the same city in favorites to refresh it
        getWeather(city)
    }

    // =========================================================
    // 🌤 WEATHER CORE
    // =========================================================

    private fun getWeather(city: String, isMyLocation: Boolean = false) {
        state = state.copy(isRefreshing = true, error = null)

        viewModelScope.launch {
            try {
                val lang = getCurrentLang()
                val locale = Locale(lang)

                val currentResponse = ApiClient.api.getCurrentWeather(apiKey, city, lang)
                val forecastResponse = ApiClient.api.getForecast(apiKey, city, 7, lang)

                val conditionText = currentResponse.current.condition.text
                val isDay = currentResponse.current.is_day
                val background = getWeatherBackground(conditionText, isDay)

                val localTime = currentResponse.location.localtime
                val timePart = localTime.substringAfter(" ")
                val currentHour = LocalTime.now().hour

                val formatter = DateTimeFormatter.ofPattern("EEEE", locale)
                val dayName = LocalDate.now().format(formatter)

                val forecastDays = forecastResponse.forecast.forecastday
                val today = forecastDays.firstOrNull()

                val weekForecast = forecastDays.map {
                    val localDate = LocalDate.parse(it.date)
                    ForecastDay(
                        day = localDate.format(formatter),
                        iconUrl = "https:${it.day.condition.icon}",
                        maxTemp = "${it.day.maxtemp_c.toInt()}°",
                        minTemp = "${it.day.mintemp_c.toInt()}°",
                        windSpeed = "${it.day.maxwind_kph.toInt()} km/h"
                    )
                }

                val hourlyForecast = today?.hour?.let { mapHourly(it) } ?: emptyList()

                state = state.copy(
                    temperature = "${currentResponse.current.temp_c.toInt()}°",
                    city = currentResponse.location.name,
                    condition = conditionText,
                    day = dayName,
                    time = timePart,
                    iconUrl = "https:${currentResponse.current.condition.icon}",
                    backgroundColors = background,
                    maxTemp = "${today?.day?.maxtemp_c?.toInt() ?: 0}°",
                    minTemp = "${today?.day?.mintemp_c?.toInt() ?: 0}°",
                    weekForecast = weekForecast,
                    hourlyForecast = hourlyForecast,
                    currentHour = currentHour,
                    humidity = "${currentResponse.current.humidity}%",
                    windSpeed = "${currentResponse.current.wind_kph.toInt()} km/h",
                    uvIndex = currentResponse.current.uv.toString(),
                    rainChance = today?.day?.daily_chance_of_rain ?: 0,
                    feelsLike = "${currentResponse.current.feelslike_c.toInt()}°",
                    pressure = "${currentResponse.current.pressure_mb.toInt()} mb",
                    isMyLocation = isMyLocation,
                    isRefreshing = false,
                    error = null
                )
            } catch (e: Exception) {
                state = state.copy(
                    error = "Error loading weather",
                    isRefreshing = false
                )
            }
        }
    }

    private fun mapHourly(hourList: List<Hour>): List<HourlyData> {
        return hourList.map {
            val time = it.time.substringAfter(" ")
            val tempValue = it.temp_c.toFloat()
            val precipitation = it.precip_mm
            val snow = if (tempValue <= 0f) precipitation else 0f
            val rain = if (tempValue > 0f) precipitation else 0f

            HourlyData(
                time = time,
                temp = "${tempValue.toInt()}°",
                iconUrl = "https:${it.condition.icon}",
                wind = "${it.wind_kph.toInt()} km/h",
                rain = rain,
                snow = snow
            )
        }
    }

    // =========================================================
    // ⏱ JOBS Management
    // =========================================================

    private fun startAutoRefresh() {
        autoRefreshJob?.cancel()
        autoRefreshJob = viewModelScope.launch {
            while (true) {
                delay(5 * 60 * 1000L)
                val city = state.city
                if (city.isNotBlank()) {
                    getWeather(city, state.isMyLocation)
                }
            }
        }
    }

    private fun startClock() {
        clockJob?.cancel()
        clockJob = viewModelScope.launch {
            while (true) {
                delay(60_000L)
                state = state.copy(currentHour = LocalTime.now().hour)
            }
        }
    }

    fun changeMode(mode: WeatherMode) {
        state = state.copy(selectedMode = mode)
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
        autoRefreshJob?.cancel()
        clockJob?.cancel()
    }
}