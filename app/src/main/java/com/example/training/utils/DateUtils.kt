package com.example.training.utils

import java.text.SimpleDateFormat
import java.util.*

private val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
private val outputFormat = SimpleDateFormat("EEE", Locale.getDefault())
private val fullDayFormat = SimpleDateFormat("EEEE", Locale.getDefault())

fun getCurrentDayName(): String {
    return fullDayFormat.format(Date())
}

fun formatDay(date: String): String {
    val parsedDate = inputFormat.parse(date)
    return parsedDate?.let { outputFormat.format(it) } ?: ""
}