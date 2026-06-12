package com.example.skyline.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.example.skyline.R
import com.example.skyline.domain.model.ForecastDay

@Composable
fun WeekForecastSection(
    weekData: List<ForecastDay>,
    onDayClick: (ForecastDay) -> Unit
) {

    Column(modifier = Modifier.fillMaxWidth()) {

        // ================= HEADER =================
        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "3-Day Forecast",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ================= HORIZONTAL CARDS =================
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(weekData) { day ->

                ForecastDayCard(
                    day = day,
                    onClick = { onDayClick(day) }
                )
            }
        }
    }
}