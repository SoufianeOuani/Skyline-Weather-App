package com.example.training.presentation.ui.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.training.domain.model.ForecastDay
import com.example.training.presentation.ui.components.*
import com.example.training.presentation.ui.components.ForecastDetailsScreen

@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {

    val state = viewModel.state
    val scrollState = rememberScrollState()

    // 🔥 Navigation state
    var selectedDay by remember { mutableStateOf<ForecastDay?>(null) }

    // 🔥 Handle system back
    BackHandler(enabled = selectedDay != null) {
        selectedDay = null
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(state.backgroundColors)
            )
            // 🔥 CLOSE DROPDOWN WHEN CLICKING OUTSIDE
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                viewModel.onSearchQueryChange("")
            }
    ) {

        // =====================================================
        // 📅 DETAILS SCREEN
        // =====================================================
        if (selectedDay != null) {

            ForecastDetailsScreen(
                day = selectedDay!!.day,
                background = state.backgroundColors,
                hourly = state.hourlyForecast, // ✅ UPDATED
                onBack = { selectedDay = null }
            )

        } else {

            // =====================================================
            // 📜 MAIN CONTENT
            // =====================================================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        state = scrollState,
                        enabled = state.suggestions.isEmpty()
                    )
                    .padding(horizontal = 16.dp)
                    .padding(top = 110.dp) // 🔥 more breathing space
            ) {

                // 🌤️ Main Weather
                MainWeatherCard(
                    state = state,
                    onAddToFavorites = viewModel::addToFavorites
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ⭐ Favorites
                FavoritesSection(
                    favorites = state.favorites,
                    onCityClick = viewModel::onFavoriteClicked,
                    onRemove = viewModel::removeFromFavorites
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 📅 Forecast
                WeekForecastSection(
                    weekData = state.weekForecast,
                    onDayClick = { selectedDay = it }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // =====================================================
            // 🔍 FIXED TOP BAR
            // =====================================================
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                state.backgroundColors.first(),
                                state.backgroundColors.first().copy(alpha = 0.95f),
                                Color.Transparent
                            )
                        )
                    )
            ) {

                TopBarSection(
                    state = state,
                    onQueryChange = viewModel::onSearchQueryChange,
                    onCitySelected = viewModel::onCitySelected,
                    onClearFocus = {
                        viewModel.onSearchQueryChange("")
                    },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}