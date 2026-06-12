package com.example.skyline.utils

import android.content.Context
import androidx.core.content.edit
import java.util.Calendar

object PreferenceHelper {

    private const val PREF = "skyline_prefs"

    // 📍 CITY
    private const val CITY = "city"

    // 🔔 NOTIFICATION CONTROL
    private const val LAST_MESSAGE = "last_message"
    private const val LAST_NOTIFICATION_TIME = "last_notification_time"

    // 🌡 OPTIONAL (future smart logic)
    private const val LAST_TEMP = "last_temp"

    // ================= CITY =================

    fun saveCity(context: Context, city: String) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit {
                putString(CITY, city)
            }
    }

    fun getCity(context: Context): String {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getString(CITY, "Rabat") ?: "Rabat"
    }

    // ================= MESSAGE =================

    fun saveLastMessage(context: Context, message: String) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit {
                putString(LAST_MESSAGE, message)
            }
    }

    fun getLastMessage(context: Context): String? {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getString(LAST_MESSAGE, null)
    }

    // ================= TIME =================

    fun saveLastNotificationTime(context: Context) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit {
                putLong(LAST_NOTIFICATION_TIME, System.currentTimeMillis())
            }
    }

    fun getLastNotificationTime(context: Context): Long {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getLong(LAST_NOTIFICATION_TIME, 0L)
    }

    // ================= RATE LIMIT =================

    fun canSendNotification(context: Context, minIntervalMinutes: Int = 30): Boolean {
        val lastTime = getLastNotificationTime(context)
        val now = System.currentTimeMillis()

        val diff = now - lastTime
        val minutes = diff / (1000 * 60)

        return minutes >= minIntervalMinutes
    }

    // ================= QUIET HOURS =================

    fun isQuietTime(): Boolean {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        return hour in 23..23 || hour in 0..7
        // 23:00 → 07:00
    }

    // ================= OPTIONAL: TEMP TRACKING =================

    fun saveLastTemp(context: Context, temp: Double) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit {
                putFloat(LAST_TEMP, temp.toFloat())
            }
    }

    fun getLastTemp(context: Context): Float {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getFloat(LAST_TEMP, -999f)
    }
}