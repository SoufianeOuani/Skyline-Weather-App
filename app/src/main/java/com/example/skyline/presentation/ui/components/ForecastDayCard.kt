package com.example.skyline.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.skyline.domain.model.ForecastDay

@Composable
fun ForecastDayCard(
    day: ForecastDay,
    onClick: () -> Unit
) {

    val glass = Brush.verticalGradient(
        listOf(
            Color.White.copy(alpha = 0.18f),
            Color.White.copy(alpha = 0.05f)
        )
    )

    Column(
        modifier = Modifier
            .width(110.dp)
            .background(glass, RoundedCornerShape(22.dp))
            .clickable { onClick() }
            .padding(vertical = 16.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 📅 DAY
        Text(
            text = day.day,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🌤 ICON
        AsyncImage(
            model = day.iconUrl,
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🌡 TEMP
        Text(
            text = day.maxTemp,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(4.dp))

        // 🌬 WIND
        Text(
            text = day.windSpeed,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.6f)
        )
    }
}