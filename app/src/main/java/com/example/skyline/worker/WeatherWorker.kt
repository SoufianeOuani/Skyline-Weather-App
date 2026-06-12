package com.example.skyline.worker

import android.content.Context
import androidx.work.*
import com.example.skyline.data.remote.ApiClient
import com.example.skyline.data.remote.dto.Hour
import com.example.skyline.domain.model.WeatherMode
import com.example.skyline.utils.NotificationHelper
import com.example.skyline.utils.PreferenceHelper
import kotlin.math.abs

class WeatherWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        return try {

            val context = applicationContext
            val city = PreferenceHelper.getCity(context)
            val apiKey = "YOUR_API_KEY"

            val current = ApiClient.api.getCurrentWeather(apiKey, city)
            val forecast = ApiClient.api.getForecast(apiKey, city, 1)

            val temp = current.current.temp_c
            val condition = current.current.condition.text

            val hourly = forecast.forecast.forecastday.firstOrNull()?.hour ?: emptyList()

            val result = buildSmartMessage(temp, condition, city, hourly)

            if (result != null && shouldNotify(context, result.message)) {

                NotificationHelper.sendRealNotification(
                    context,
                    result.message,
                    city,
                    result.mode
                )

                PreferenceHelper.saveLastMessage(context, result.message)
                PreferenceHelper.saveLastNotificationTime(context)
                PreferenceHelper.saveLastTemp(context, temp)
            }

            Result.success()

        } catch (e: Exception) {
            Result.retry()
        }
    }

    data class SmartResult(val message: String, val mode: WeatherMode)

    private fun buildSmartMessage(
        temp: Double,
        condition: String,
        city: String,
        hourly: List<Hour>
    ): SmartResult? {

        val rain = hourly.take(6).firstOrNull {
            it.will_it_rain == 1 && it.precip_mm > 0.5
        }

        if (rain != null) {
            val hour = rain.time.substringAfter(" ")
            return SmartResult(
                "🌧 Rain expected at $hour in $city",
                WeatherMode.RAIN
            )
        }

        val lastTemp = PreferenceHelper.getLastTemp(applicationContext)

        if (lastTemp != -999f) {
            val diff = temp - lastTemp
            if (abs(diff) >= 5) {
                return if (diff > 0)
                    SmartResult("🔥 Temperature rising in $city", WeatherMode.TEMP)
                else
                    SmartResult("❄️ Temperature dropping in $city", WeatherMode.TEMP)
            }
        }

        if (temp > 35) {
            return SmartResult("🔥 Heat alert in $city", WeatherMode.TEMP)
        }

        return null
    }

    private fun shouldNotify(context: Context, newMessage: String): Boolean {

        if (newMessage == PreferenceHelper.getLastMessage(context)) return false
        if (PreferenceHelper.isQuietTime()) return false
        if (!PreferenceHelper.canSendNotification(context)) return false

        return true
    }
}