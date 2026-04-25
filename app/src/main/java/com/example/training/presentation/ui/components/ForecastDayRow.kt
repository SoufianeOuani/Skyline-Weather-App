package com.example.training.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.training.domain.model.ForecastDay

@Composable
fun ForecastDayRow(
    day: ForecastDay,
    onClick: () -> Unit
) {

    val glass = Brush.horizontalGradient(
        listOf(
            Color.White.copy(alpha = 0.15f),
            Color.White.copy(alpha = 0.05f)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(
                1.dp,
                Color.White.copy(alpha = 0.2f),
                RoundedCornerShape(18.dp)
            )
            .background(glass, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 📅 Day
            Text(
                text = day.day,
                modifier = Modifier.weight(1f),
                color = Color.White.copy(alpha = 0.7f)
            )

            // 🌤️ Icon
            AsyncImage(
                model = day.iconUrl,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            // 🌡️ Temp
            Text(
                text = "${day.maxTemp} / ${day.minTemp}",
                modifier = Modifier.weight(1f),
                color = Color.White
            )

            // 💨 Wind
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Air,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = day.windSpeed,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}