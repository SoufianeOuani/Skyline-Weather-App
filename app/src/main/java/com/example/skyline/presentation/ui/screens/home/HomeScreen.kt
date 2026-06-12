package com.example.skyline.presentation.ui.screens.home

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.skyline.domain.model.ForecastDay
import com.example.skyline.presentation.ui.components.*
import com.example.skyline.presentation.ui.theme.AppBackground
import kotlin.math.exp

fun elastic(progress: Float): Float = (1 - exp(-4 * progress)).coerceIn(0f, 1f)

@SuppressLint("ContextCastToActivity")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state = viewModel.state
    val scrollState = rememberScrollState()
    val activity = LocalContext.current as Activity

    var showRestartDialog by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableStateOf<ForecastDay?>(null) }

    // 🔥 Listen for restart events (e.g., after permission is granted)
    LaunchedEffect(Unit) {
        viewModel.restartEvents.collect {
            showRestartDialog = true
        }
    }

    // 🔥 Initial load logic
    LaunchedEffect(Unit) {
        if (state.city.isBlank()) {
            viewModel.reloadLocation()
        }
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = { viewModel.refreshWeather() }
    )

    BackHandler(enabled = selectedDay != null) { selectedDay = null }

    // Physics for Pull-to-Refresh animations
    val rawProgress = pullRefreshState.progress.coerceIn(0f, 1.4f)
    val elasticProgress = elastic(rawProgress)
    val dragOffset by animateDpAsState(
        targetValue = (elasticProgress * 140).dp,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)
    )
    val stretchScale by animateFloatAsState(
        targetValue = 1f + (elasticProgress * 0.05f),
        animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessVeryLow)
    )

    AppBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(state.backgroundColors))
                .pullRefresh(pullRefreshState)
        ) {
            // ================= MAIN SCROLLING CONTENT =================
            AnimatedVisibility(
                visible = selectedDay == null,
                enter = fadeIn(), exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            translationY = dragOffset.toPx()
                            scaleX = stretchScale
                            scaleY = stretchScale
                        }
                        .verticalScroll(scrollState, enabled = state.suggestions.isEmpty())
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { if (state.suggestions.isNotEmpty()) viewModel.onSearchQueryChange("") }
                        .padding(horizontal = 20.dp)
                        .padding(top = 110.dp, bottom = 32.dp)
                ) {
                    Spacer(modifier = Modifier.height(28.dp))

                    MainWeatherCard(
                        state = state,
                        isFavorite = viewModel.isFavorite(),
                        onToggleFavorite = { viewModel.toggleFavorite() }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // ⭐ Premium Favorite Section
                    FavoriteCitiesSection(
                        favorites = viewModel.favoriteCities,
                        onCityClick = { viewModel.loadWeather(state.city) },
                        onRemove = { viewModel.toggleFavorite(state.city) }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    WeekForecastSection(
                        weekData = state.weekForecast,
                        onDayClick = { selectedDay = it }
                    )
                }
            }

            // ================= PULL REFRESH INDICATOR =================
            if (state.isRefreshing || rawProgress > 0.01f) {
                CustomRefreshIndicator(
                    isRefreshing = state.isRefreshing,
                    progress = rawProgress,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .zIndex(10f)
                        .offset(y = 40.dp)
                )
            }

            // ================= PINNED TOP BAR =================
            AnimatedVisibility(
                visible = selectedDay == null,
                enter = fadeIn(), exit = fadeOut()
            ) {
                TopBarSection(
                    state = state,
                    onQueryChange = viewModel::onSearchQueryChange,
                    onCitySelected = viewModel::onCitySelected,
                    onClearFocus = { viewModel.onSearchQueryChange("") },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset { IntOffset(0, dragOffset.toPx().toInt()) }
                        .zIndex(5f)
                )
            }

            // ================= DETAILS OVERLAY =================
            AnimatedVisibility(
                visible = selectedDay != null,
                enter = fadeIn() + scaleIn(), exit = fadeOut() + scaleOut()
            ) {
                selectedDay?.let {
                    ForecastDetailsScreen(
                        day = it.day,
                        background = state.backgroundColors,
                        hourly = state.hourlyForecast,
                        selectedMode = state.selectedMode,
                        currentHour = state.currentHour,
                        onModeChange = viewModel::changeMode,
                        onBack = { selectedDay = null },
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    // ================= RESTART DIALOG =================
    if (showRestartDialog) {
        AlertDialog(
            onDismissRequest = { showRestartDialog = false },
            title = { Text("Location Permission Granted") },
            text = { Text("To sync your local weather data, we need to quickly refresh the app.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRestartDialog = false
                        // Professional Activity Restart
                        val intent = activity.intent
                        activity.finish()
                        activity.startActivity(intent)
                        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                ) { Text("Refresh Now", fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showRestartDialog = false }) { Text("Later") }
            }
        )
    }
}