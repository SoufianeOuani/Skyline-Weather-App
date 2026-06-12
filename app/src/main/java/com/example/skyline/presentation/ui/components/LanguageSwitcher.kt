package com.example.skyline.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LanguageSwitcher(
    currentLang: String,
    onLangChange: (String) -> Unit
) {
    val languages = listOf("en", "fr", "ar", "es")

    Row(
        modifier = Modifier
            .background(
                Color.White.copy(alpha = 0.12f),
                RoundedCornerShape(50)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        languages.forEach { lang ->

            val isSelected = currentLang == lang

            val animatedAlpha by animateFloatAsState(
                targetValue = if (isSelected) 1f else 0.4f,
                label = ""
            )

            val animatedScale by animateFloatAsState(
                targetValue = if (isSelected) 1f else 0.95f,
                label = ""
            )

            Box(
                modifier = Modifier
                    .scale(animatedScale)
                    .background(
                        if (isSelected)
                            Color.White.copy(alpha = 0.25f)
                        else
                            Color.Transparent,
                        RoundedCornerShape(50)
                    )
                    .clickable { onLangChange(lang) }
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = lang.uppercase(),
                    color = Color.White.copy(alpha = animatedAlpha),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}