package com.example.training.utils

fun formatTime(dateTime: String): String {
    // API format: "2026-04-23 16:03"
    return dateTime.substringAfter(" ")
}