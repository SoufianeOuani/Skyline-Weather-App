package com.example.training.data.remote.api

import androidx.compose.ui.input.key.Key
import com.example.training.data.remote.dto.ForecastResponse
import com.example.training.data.remote.dto.SearchResponse
import com.example.training.data.remote.dto.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") city: String
    ): WeatherResponse

    @GET("forecast.json")
    suspend fun getForecast(
        @Query("key") apiKey: String,
        @Query("q") city: String,
        @Query("days") days: Int = 7
    ): ForecastResponse

    @GET("search.json")
    suspend fun searchCity(
        @Query("key") apiKey: String,
        @Query("q") query: String
    ): List<SearchResponse>
}