package com.example.skyline.data.remote.api

import com.example.skyline.data.remote.dto.ForecastResponse
import com.example.skyline.data.remote.dto.SearchResponse
import com.example.skyline.data.remote.dto.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") city: String,
        @Query("aqi") aqi: String = "no",
        @Query("lang") lang: String = "en"
    ): WeatherResponse

    @GET("forecast.json")
    suspend fun getForecast(
        @Query("key") apiKey: String,
        @Query("q") city: String,
        @Query("days") days: Int,
        @Query("aqi") aqi: String = "no",
        @Query("alerts") alerts: String = "no",
        @Query("lang") lang: String = "en"
    ): ForecastResponse

    @GET("search.json")
    suspend fun searchCity(
        @Query("key") apiKey: String,
        @Query("q") query: String
    ): List<SearchResponse>
}