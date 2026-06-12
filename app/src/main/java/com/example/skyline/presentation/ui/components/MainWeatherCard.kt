package com.example.skyline.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.skyline.R
import com.example.skyline.presentation.ui.screens.home.HomeState
import androidx.compose.ui.res.stringResource
import com.example.skyline.utils.mapConditionToStringRes

@Composable
fun MainWeatherCard(
    state: HomeState,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {

    Box {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(28.dp)
                )
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.2f),
                    RoundedCornerShape(28.dp)
                )
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ================= DATE + TIME =================
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // 🔹 LEFT SIDE (DAY + TIME)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = state.day,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.7f)
                    )

                    Text(
                        text = "•",
                        color = Color.White.copy(alpha = 0.4f)
                    )

                    Text(
                        text = state.time,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }

                // 🔹 RIGHT SIDE (FAVORITE)
                IconButton(
                    onClick = onToggleFavorite
                ) {
                    Icon(
                        imageVector = if (isFavorite)
                            Icons.Default.Star
                        else
                            Icons.Default.StarBorder,
                        contentDescription = null,
                        tint = if (isFavorite)
                            Color(0xFFFFD54F)
                        else
                            Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🌤 ICON
            AsyncImage(
                model = state.iconUrl,
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 🌡 TEMP
            Text(
                text = state.temperature,
                style = MaterialTheme.typography.displayLarge,
                color = Color.White
            )

            // ☁️ CONDITION
            Text(
                text = stringResource(mapConditionToStringRes(state.condition)),
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyLarge
            )

            // 🌡 MIN / MAX
            Text(
                text = stringResource(
                    R.string.temp_range,
                    state.minTemp,
                    state.maxTemp
                ),
                color = Color.White.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ================= STATS =================
            WeatherStatsInline(state)
        }


    }
}