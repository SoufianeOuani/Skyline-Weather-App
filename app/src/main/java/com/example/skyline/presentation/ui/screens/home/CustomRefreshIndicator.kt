package com.example.skyline.presentation.ui.components

import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.exp
import com.example.skyline.R

// 🌊 Elastic curve
fun elastic(progress: Float): Float =
    (1 - exp(-4 * progress)).coerceIn(0f, 1f)

@Composable
fun CustomRefreshIndicator(
    isRefreshing: Boolean,
    progress: Float,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current

    val raw = progress.coerceIn(0f, 1.3f) // allow a bit of overshoot
    val e = elastic(raw)

    // 🎯 Threshold
    val threshold = 0.9f
    val isReady = raw >= threshold

    // ⚡ Velocity-ish: more pull → faster spin
    val spinDuration = (900 - (e * 400)).toInt().coerceAtLeast(450)

    val infinite = rememberInfiniteTransition(label = "spin")

    val spin by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(spinDuration, easing = LinearEasing)
        ),
        label = "spin"
    )

    // 🔄 Scale with bounce on release
    val targetScale =
        if (isRefreshing) 1f
        else 0.75f + (e * 0.5f)

    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "scale"
    )

    val alpha = e

    // ✨ Glow intensity
    val glowIntensity = if (isReady) 1f else e

    val glowPulse by infinite.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    // 🎨 Accent color morph
    val accent = if (isReady) Color(0xFF38BDF8) else Color(0xFF60A5FA)

    // 📳 Haptic when crossing threshold
    var triggered by remember { mutableStateOf(false) }
    LaunchedEffect(isReady, isRefreshing) {
        if (isReady && !triggered && !isRefreshing) {
            triggered = true
            view.performHapticFeedback(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                    HapticFeedbackConstants.CONFIRM
                else HapticFeedbackConstants.LONG_PRESS
            )
        }
        if (!isReady && !isRefreshing) triggered = false
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .height(300.dp),
        contentAlignment = Alignment.TopCenter
    ) {

        if (isRefreshing || raw > 0f) {

            // 🌊 LIQUID RIPPLE (background)
            Box(
                modifier = Modifier
                    .size((90 + (e * 40)).dp)
                    .graphicsLayer {
                        scaleX = 0.9f + e * 0.3f
                        scaleY = 0.9f + e * 0.3f
                        this.alpha = 0.25f * e
                    }
                    .blur(50.dp)
                    .background(Color.Transparent)
            )

            // ✨ GLOW
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .graphicsLayer {
                        scaleX = glowPulse * glowIntensity
                        scaleY = glowPulse * glowIntensity
                        this.alpha = 0.5f * glowIntensity
                    }
                    .blur(35.dp)
                    .background(Color.Transparent)
            )

            // 🔵 MAIN CIRCLE
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    }
                    .background(
                        Color.White.copy(alpha = 0.12f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(
                            if (isRefreshing) spin
                            else e * 220f // expressive rotation while pulling
                        )
                )
            }

            // 🎯 TEXT MORPH
            AnimatedVisibility(
                visible = raw > 0.2f && !isRefreshing,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = when {
                        isRefreshing -> stringResource(R.string.updating)
                        isReady -> stringResource(R.string.updating)
                        else -> stringResource(R.string.updated_successfully)
                    },
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(top = 56.dp)
                )
            }
        }
    }
}