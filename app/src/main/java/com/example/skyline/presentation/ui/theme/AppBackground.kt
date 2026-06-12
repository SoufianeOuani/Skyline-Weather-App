package com.example.skyline.presentation.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun AppBackground(content: @Composable () -> Unit) {

    val vertical = Brush.verticalGradient(
        listOf(
            Color(0xFF0F2A5F),
            Color(0xFF1F3F85),
            Color(0xFF2F5DB3)
        )
    )

    val radial = Brush.radialGradient(
        colors = listOf(
            Color(0xFF3A6EDC).copy(alpha = 0.25f), // glow center
            Color.Transparent
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(vertical)
    ) {

        // ✨ center glow (this is what makes it look like your image)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(radial)
        )

        content()
    }
}