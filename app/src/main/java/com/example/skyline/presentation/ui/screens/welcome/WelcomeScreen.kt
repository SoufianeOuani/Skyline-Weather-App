package com.example.skyline.presentation.ui.screens.welcome

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit
) {

    // 🌈 SAME APP BACKGROUND (IMPORTANT)
    val background = Brush.verticalGradient(
        listOf(
            Color(0xFF0F2A5F),
            Color(0xFF1F3F85),
            Color(0xFF2F5DB3)
        )
    )

    var startAnim by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = tween(1200),
        label = ""
    )

    val scale by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0.85f,
        animationSpec = tween(1200),
        label = ""
    )

    // 🔥 AUTO TRANSITION + ANIMATION START
    LaunchedEffect(Unit) {
        startAnim = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {

        // ✨ SOFT GLOW (lighter & smoother)
        val infiniteTransition = rememberInfiniteTransition(label = "glow")

        val glowScale by infiniteTransition.animateFloat(
            initialValue = 0.9f,
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            ),
            label = ""
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(300.dp)
                .scale(glowScale)
                .alpha(0.25f)
                .blur(80.dp)
                .background(
                    Brush.radialGradient(
                        listOf(
                            Color(0xFF60A5FA),
                            Color.Transparent
                        )
                    )
                )
        )

        // ================= CONTENT =================
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // ===== TOP =====
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .alpha(alpha)
                    .scale(scale)
            ) {

                Icon(
                    imageVector = Icons.Default.Cloud,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Skyline",
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White
                )

                Text(
                    text = "Atmospheric Intelligence",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.White.copy(alpha = 0.6f),
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // ===== BOTTOM =====
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Experience weather through\na new lens of clarity.",
                    fontSize = 17.sp,
                    lineHeight = 26.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White.copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // 🔘 GLASS BUTTON
                Surface(
                    onClick = onGetStarted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(30.dp),
                    color = Color.White.copy(alpha = 0.12f),
                    border = BorderStroke(
                        1.dp,
                        Color.White.copy(alpha = 0.25f)
                    )
                ) {

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "GET STARTED",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = Color(0xFF60A5FA)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "v2.0 Skyline",
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.3f)
                )
            }
        }
    }
}