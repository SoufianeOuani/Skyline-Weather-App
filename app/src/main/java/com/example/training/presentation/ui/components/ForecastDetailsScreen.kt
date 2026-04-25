package com.example.training.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.training.domain.model.HourlyData
import java.time.LocalTime

@Composable
fun ForecastDetailsScreen(
    day: String,
    hourly: List<HourlyData>,
    background: List<Color>,
    onBack: () -> Unit
) {

    val listState = rememberLazyListState()

    // 🔥 Snap behavior (iOS feel)
    val flingBehavior = rememberSnapFlingBehavior(listState)

    val currentHour = remember { LocalTime.now().hour }

    val currentIndex = hourly.indexOfFirst {
        it.time.substringBefore(":").toIntOrNull() == currentHour
    }

    // 🔥 Auto-scroll to current
    LaunchedEffect(currentIndex) {
        if (currentIndex >= 0) {
            listState.scrollToItem(currentIndex)
        }
    }

    // 🔥 Selected index (center feeling)
    val selectedIndex by remember {
        derivedStateOf { listState.firstVisibleItemIndex }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(background))
            .padding(16.dp)
    ) {

        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, null, tint = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = day,
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        // =========================
        // 🚀 ULTRA GRAPH
        // =========================
        if (hourly.isNotEmpty()) {

            val animatedProgress by animateFloatAsState(
                targetValue = 1f,
                animationSpec = tween(1000),
                label = ""
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {

                val temps = hourly.map {
                    it.temp.replace("°", "").toFloat()
                }

                val maxTemp = temps.maxOrNull() ?: 0f
                val minTemp = temps.minOrNull() ?: 0f

                val stepX = size.width / (temps.size - 1)

                val linePath = Path()
                val fillPath = Path()
                val points = mutableListOf<Offset>()

                temps.forEachIndexed { index, temp ->

                    val x = index * stepX
                    val normalized = (temp - minTemp) / (maxTemp - minTemp + 0.1f)
                    val y = size.height - (normalized * size.height)

                    val point = Offset(x * animatedProgress, y)
                    points.add(point)

                    if (index == 0) {
                        linePath.moveTo(point.x, point.y)
                        fillPath.moveTo(point.x, size.height)
                        fillPath.lineTo(point.x, point.y)
                    } else {
                        linePath.lineTo(point.x, point.y)
                        fillPath.lineTo(point.x, point.y)
                    }
                }

                fillPath.lineTo(points.last().x, size.height)
                fillPath.close()

                // 🌈 Fill
                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    )
                )

                // Glow
                drawPath(
                    path = linePath,
                    color = Color.White.copy(alpha = 0.3f),
                    style = Stroke(width = 10f)
                )

                // Main line
                drawPath(
                    path = linePath,
                    color = Color.White,
                    style = Stroke(width = 4f)
                )

                // 🔥 Vertical indicator line
                val selectedPoint = points.getOrNull(selectedIndex)

                selectedPoint?.let {
                    drawLine(
                        color = Color.White.copy(alpha = 0.4f),
                        start = Offset(it.x, 0f),
                        end = Offset(it.x, size.height),
                        strokeWidth = 2f
                    )
                }

                // Points
                points.forEachIndexed { index, point ->

                    val isSelected = index == selectedIndex

                    drawCircle(
                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.4f),
                        radius = if (isSelected) 10f else 5f,
                        center = point
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🔥 Floating label
            val selectedHour = hourly.getOrNull(selectedIndex)

            selectedHour?.let {
                Text(
                    text = "${it.temp} • ${it.time}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        Text(
            text = "Hourly Forecast",
            color = Color.White.copy(alpha = 0.85f),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {

            itemsIndexed(hourly) { index, hour ->

                val isSelected = index == selectedIndex

                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.15f else 1f,
                    label = ""
                )

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected)
                            Color.White.copy(alpha = 0.3f)
                        else
                            Color.White.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier
                        .width(72.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                ) {

                    Column(
                        modifier = Modifier
                            .padding(vertical = 12.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = hour.time,
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.labelMedium
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        AsyncImage(
                            model = hour.iconUrl,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = hour.temp,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}