package com.example.training.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.training.domain.model.ForecastDay

@Composable
fun WeekForecastSection(
    weekData: List<ForecastDay>,
    onDayClick: (ForecastDay) -> Unit
) {

    Column {

        Text(
            text = "7-Day Forecast",
            color = Color.White
        )

        Spacer(modifier = Modifier.height(12.dp))

        weekData.forEach { day ->

            ForecastDayRow(
                day = day,
                onClick = { onDayClick(day) }
            )
        }
    }
}