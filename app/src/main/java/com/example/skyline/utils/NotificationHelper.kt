package com.example.skyline.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.skyline.domain.model.WeatherMode

object NotificationHelper {

    private const val CHANNEL_ID = "weather_channel"
    private const val CHANNEL_NAME = "Weather Updates"

    /**
     * Call this in your Application class or MainActivity onCreate
     * to ensure the channel exists before sending notifications.
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Daily weather alerts and updates"
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun sendRealNotification(
        context: Context,
        message: String,
        city: String,
        mode: WeatherMode
    ) {
        // 1. Setup the deep-link intent
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("city", city)
            putExtra("mode", mode.name)
            // Essential for opening the app from a notification properly
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            // Use unique ID if you want multiple notifications to lead to different cities
            city.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 2. Build the notification using the 2026 Aero-Glass style logic
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Consider a custom transparent cloud icon
            .setContentTitle("Skyline: $city")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 3. Post notification with a unique ID to avoid overwriting
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
