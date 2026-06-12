package com.example.skyline
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.skyline.R


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.*
import com.example.skyline.domain.model.WeatherMode
import com.example.skyline.presentation.ui.screens.home.HomeScreen
import com.example.skyline.presentation.ui.screens.home.HomeViewModel
import com.example.skyline.presentation.ui.screens.welcome.WelcomeScreen
import com.example.skyline.ui.theme.TrainingTheme
import com.example.skyline.utils.LanguageManager
import com.example.skyline.utils.NotificationHelper
import com.example.skyline.worker.WeatherWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    // 🌍 Apply language BEFORE UI
    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("settings", MODE_PRIVATE)

        // 🔥 Auto language on first launch
        if (!prefs.contains("lang")) {
            val detected = LanguageManager.getDeviceLanguage()
            prefs.edit().putString("lang", detected).apply()
        }

        val lang = prefs.getString("lang", "en") ?: "en"
        val context = LanguageManager.setLocale(newBase, lang)

        super.attachBaseContext(context)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        NotificationHelper.createNotificationChannel(this)

        // 🔄 Background worker
        val workRequest = PeriodicWorkRequestBuilder<WeatherWorker>(
            15, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "weather_worker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

        setContent {

            val viewModel: HomeViewModel = viewModel()
            val context = this@MainActivity

            val prefs = context.getSharedPreferences("app_prefs", MODE_PRIVATE)
            val langPrefs = context.getSharedPreferences("settings", MODE_PRIVATE)

            val lang = langPrefs.getString("lang", "en") ?: "en"

            // 🔥 RTL FIX
            val layoutDirection = if (lang == "ar") {
                LayoutDirection.Rtl
            } else {
                LayoutDirection.Ltr
            }

            CompositionLocalProvider(
                LocalLayoutDirection provides layoutDirection
            ) {

                var showWelcome by remember {
                    mutableStateOf(!prefs.getBoolean("hasSeenWelcome", false))
                }

                val locationLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    val granted = permissions.values.any { it }
                    if (granted) {
                        if (isLocationEnabled(context)) {
                            viewModel.reloadLocation()
                        } else {
                            context.startActivity(
                                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            )
                        }
                    }
                }

                val notificationLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) {}

                TrainingTheme {

                    AnimatedContent(
                        targetState = showWelcome,
                        transitionSpec = {
                            fadeIn(tween(500)) togetherWith fadeOut(tween(400))
                        },
                        label = "navigation"
                    ) { isWelcome ->

                        if (isWelcome) {

                            WelcomeScreen(
                                onGetStarted = {

                                    prefs.edit {
                                        putBoolean("hasSeenWelcome", true)
                                    }

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        val notifPermission =
                                            Manifest.permission.POST_NOTIFICATIONS

                                        if (ContextCompat.checkSelfPermission(
                                                context,
                                                notifPermission
                                            ) != PackageManager.PERMISSION_GRANTED
                                        ) {
                                            notificationLauncher.launch(notifPermission)
                                        }
                                    }

                                    locationLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )

                                    showWelcome = false
                                }
                            )

                        } else {
                            HomeScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}
