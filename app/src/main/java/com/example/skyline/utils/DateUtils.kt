package com.example.skyline.utils

import java.text.SimpleDateFormat
import java.util.*

private val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

// ✅ FULL DAY NAME
private val fullDayFormat = SimpleDateFormat("EEEE", Locale.getDefault())

fun getCurrentDayName(): String {
    return fullDayFormat.format(Date())
}

// 🔥 FIXED → now returns FULL day name
fun formatDay(date: String): String {
    val parsedDate = inputFormat.parse(date)
    return parsedDate?.let { fullDayFormat.format(it) } ?: ""
}