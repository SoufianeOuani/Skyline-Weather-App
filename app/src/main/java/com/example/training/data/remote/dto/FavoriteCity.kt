package com.example.training.data.remote.dto

/**
 * ⭐ FavoriteCity
 *
 * Represents a user-saved city with its weather info.
 *
 * @param name City name
 * @param temp Current temperature
 * @param iconUrl Weather icon
 */
data class FavoriteCity(
    val name: String,
    val temp: String,
    val iconUrl: String
)