package com.example.skyline.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.skyline.R
import com.example.skyline.presentation.ui.screens.home.HomeState

fun safeFloat(value: String?): Float {
    return value
        ?.filter { it.isDigit() || it == '.' }
        ?.toFloatOrNull()
        ?: 0f
}

fun percent(value: String?): Float {
    return safeFloat(value) / 100f
}

@Composable
fun WeatherStatsInline(state: HomeState) {

    val feels = safeFloat(state.feelsLike)
    val feelsProgress = ((feels + 10f) / 60f).coerceIn(0f, 1f)

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.weight(1f)) {
                StatMiniCard(
                    stringResource(R.string.humidity),
                    state.humidity,
                    percent(state.humidity)
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                StatMiniCard(
                    stringResource(R.string.wind),
                    state.windSpeed,
                    percent(state.windSpeed)
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                StatMiniCard(
                    stringResource(R.string.uv_index),
                    state.uvIndex,
                    percent(state.uvIndex)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.weight(1f)) {
                StatMiniCard(
                    stringResource(R.string.rain),
                    "${state.rainChance}%",
                    state.rainChance / 100f
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                StatMiniCard(
                    stringResource(R.string.feels_like),
                    state.feelsLike,
                    feelsProgress
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                StatMiniCard(
                    stringResource(R.string.pressure),
                    state.pressure,
                    (safeFloat(state.pressure) - 900f) / 200f
                )
            }
        }
    }
}