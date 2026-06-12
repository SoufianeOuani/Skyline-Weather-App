package com.example.skyline.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.skyline.R
import com.example.skyline.domain.model.WeatherMode

@Composable
fun GlassWeatherToggle(
    selectedMode: WeatherMode,
    onSelect: (WeatherMode) -> Unit
) {

    val options = listOf(
        WeatherMode.TEMP to R.string.mode_temp,
        WeatherMode.WIND to R.string.mode_wind,
        WeatherMode.RAIN to R.string.mode_rain,
        WeatherMode.SNOW to R.string.mode_snow
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.White.copy(alpha = 0.12f),
                shape = RoundedCornerShape(50)
            )
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        options.forEach { (mode, labelRes) ->

            val isSelected = mode == selectedMode

            val animatedAlpha by animateFloatAsState(
                targetValue = if (isSelected) 1f else 0.6f,
                label = ""
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (isSelected)
                            Color.White.copy(alpha = 0.25f)
                        else
                            Color.Transparent
                    )
                    .clickable { onSelect(mode) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = stringResource(labelRes), // 🔥 translated
                    color = Color.White.copy(alpha = animatedAlpha),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}