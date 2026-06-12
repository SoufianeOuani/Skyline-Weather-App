package com.example.skyline.presentation.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun StatMiniCard(
    title: String,
    value: String,
    progress: Float
) {

//    val (icon, color) = when (title) {
//        "Humidity" -> Icons.Default.Water to Color(0xFF60A5FA)
//        "Wind" -> Icons.Default.Air to Color(0xFF34D399)
//        "UV" -> Icons.Default.WbSunny to Color(0xFFFBBF24)
//        "Rain" -> Icons.Default.Grain to Color(0xFF38BDF8)
//        "Feels" -> Icons.Default.Thermostat to Color(0xFFF87171)
//        "Pressure" -> Icons.Default.Speed to Color(0xFFA78BFA)
//        else -> Icons.Default.Info to Color.White
//    }

    val (icon, color) = when (title.lowercase()) {

        // 🌊 HUMIDITY
        "humidity", "humidité", "humedad", "الرطوبة" ->
            Icons.Default.Water to Color(0xFF60A5FA)

        // 🌬 WIND
        "wind", "vent", "viento", "الرياح" ->
            Icons.Default.Air to Color(0xFF34D399)

        // ☀️ UV
        "uv index", "indice uv", "índice uv", "مؤشر الأشعة" ->
            Icons.Default.WbSunny to Color(0xFFFBBF24)

        // 🌧 RAIN
        "rain", "pluie", "lluvia", "المطر" ->
            Icons.Default.Grain to Color(0xFF38BDF8)

        // 🌡 FEELS LIKE
        "feels like", "ressenti", "sensación", "الإحساس" ->
            Icons.Default.Thermostat to Color(0xFFF87171)

        // ⚡ PRESSURE
        "pressure", "pression", "presión", "الضغط" ->
            Icons.Default.Speed to Color(0xFFA78BFA)

        else -> Icons.Default.Info to Color.White
    }
    // 🎬 Smooth progress animation
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(800),
        label = ""
    )

    // ✨ Subtle pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.White.copy(alpha = 0.08f),
                RoundedCornerShape(18.dp)
            )
            .border(
                1.dp,
                Color.White.copy(alpha = 0.15f),
                RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        // 🔹 ICON + TITLE
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color.copy(alpha = 0.9f),
                modifier = Modifier
                    .size(18.dp)
                    .background(
                        color.copy(alpha = 0.15f),
                        shape = CircleShape
                    )
                    .padding(4.dp)
            )

            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodySmall
            )
        }

        // 🔹 VALUE
        Text(
            text = value,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )

        // 🔥 PREMIUM PROGRESS BAR
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White.copy(alpha = 0.1f))
        ) {

            // ✨ GLOW LAYER
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .graphicsLayer {
                        scaleX = pulse
                        scaleY = pulse
                        alpha = 0.6f
                    }
                    .blur(8.dp)
                    .background(color.copy(alpha = 0.6f))
            )

            // 🌈 MAIN GRADIENT BAR
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                color.copy(alpha = 0.5f),
                                color
                            )
                        )
                    )
            )
        }
    }
}