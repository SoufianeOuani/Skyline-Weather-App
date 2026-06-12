package com.example.skyline.presentation.ui.components

import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.skyline.domain.model.HourlyData
import com.example.skyline.domain.model.WeatherMode
import kotlinx.coroutines.flow.first
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.skyline.presentation.ui.screens.home.HomeViewModel
import com.example.skyline.presentation.ui.theme.AppBackground
import kotlin.math.abs
import com.example.skyline.R

// ================= COLORS =================

fun getTempColor(v: Float) = when {
    v < 0 -> Color(0xFF1976D2)
    v < 10 -> Color(0xFF64B5F6)
    v < 20 -> Color(0xFF81C784)
    v < 27 -> Color(0xFFFFEE58)
    v < 35 -> Color(0xFFFFB74D)
    else -> Color(0xFFE53935)
}

fun getWindColor(v: Float) = when {
    v < 5 -> Color(0xFF81C784)
    v < 20 -> Color(0xFFAED581)
    v < 40 -> Color(0xFFFFEE58)
    v < 60 -> Color(0xFFFFA726)
    else -> Color(0xFFE53935)
}

fun getRainColor(v: Float) = when {
    v == 0f -> Color(0xFF90CAF9)
    v < 2.5 -> Color(0xFF42A5F5)
    v < 7.6 -> Color(0xFF1E88E5)
    v < 50 -> Color(0xFF1565C0)
    else -> Color(0xFF0D47A1)
}

fun getSnowColor(v: Float) = when {
    v == 0f -> Color(0xFFE3F2FD)
    v < 2 -> Color(0xFF90CAF9)
    v < 10 -> Color(0xFF42A5F5)
    else -> Color(0xFF1565C0)
}

// ================= DESCRIPTION =================

@Composable
fun getWeatherDescription(mode: WeatherMode, value: Float): String {

    return when (mode) {

        WeatherMode.TEMP -> when {
            value < 0 -> stringResource(R.string.temp_freezing)
            value < 10 -> stringResource(R.string.temp_cold)
            value < 20 -> stringResource(R.string.temp_cool)
            value < 27 -> stringResource(R.string.temp_comfortable)
            value < 35 -> stringResource(R.string.temp_warm)
            else -> stringResource(R.string.temp_hot)
        }

        WeatherMode.WIND -> when {
            value < 5 -> stringResource(R.string.wind_calm)
            value < 20 -> stringResource(R.string.wind_light)
            value < 40 -> stringResource(R.string.wind_windy)
            value < 60 -> stringResource(R.string.wind_strong)
            else -> stringResource(R.string.wind_storm)
        }

        WeatherMode.RAIN -> when {
            value == 0f -> stringResource(R.string.rain_none)
            value < 2.5 -> stringResource(R.string.rain_light)
            value < 7.6 -> stringResource(R.string.rain_moderate)
            value < 50 -> stringResource(R.string.rain_heavy)
            else -> stringResource(R.string.rain_extreme)
        }

        WeatherMode.SNOW -> when {
            value == 0f -> stringResource(R.string.snow_none)
            value < 2 -> stringResource(R.string.snow_light)
            value < 10 -> stringResource(R.string.snowing)
            else -> stringResource(R.string.snow_heavy)
        }
    }
}

// ================= SCREEN =================

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastDetailsScreen(
    day: String,
    hourly: List<HourlyData>,
    background: List<Color>,
    selectedMode: WeatherMode,
    currentHour: Int, // 🔥 ADD THIS
    onModeChange: (WeatherMode) -> Unit,
    onBack: () -> Unit,
    viewModel: HomeViewModel
) {

    val state = viewModel.state
    val view = LocalView.current
    val listState = rememberLazyListState()
    val density = LocalDensity.current

    // ================= AUTOFOCUS FIXED =================
    LaunchedEffect(hourly, currentHour) {

        val targetIndex = hourly.indexOfFirst {
            it.time.startsWith(currentHour.toString().padStart(2, '0'))
        }

        if (targetIndex != -1) {

            snapshotFlow { listState.layoutInfo.visibleItemsInfo }
                .first { it.isNotEmpty() }

            val itemWidthPx = with(density) { 82.dp.toPx() }
            val viewportWidth = listState.layoutInfo.viewportEndOffset.toFloat()

            val offset = (viewportWidth / 2 - itemWidthPx / 2).toInt()

            listState.scrollToItem(targetIndex, -offset)
        }
    }

    val fling = rememberSnapFlingBehavior(listState)

    // ================= DRAG =================
    var dragX by remember { mutableStateOf(0f) }
    val parallaxOffset = (dragX * 0.015f).coerceIn(-20f, 20f)

    // ================= INDEX =================
    val selectedIndex by remember {
        derivedStateOf {
            val layout = listState.layoutInfo
            val items = layout.visibleItemsInfo

            if (items.isEmpty()) return@derivedStateOf 0

            val center = layout.viewportEndOffset / 2f

            items.minByOrNull {
                abs((it.offset + it.size / 2f) - center)
            }?.index ?: 0
        }
    }

    LaunchedEffect(selectedIndex) {
        view.performHapticFeedback(HapticFeedbackConstants.TEXT_HANDLE_MOVE)
    }

    val values = hourly.map {
        when (selectedMode) {
            WeatherMode.TEMP -> it.temp.replace("°", "").toFloat()
            WeatherMode.WIND -> it.wind.replace(" km/h", "").toFloatOrNull() ?: 0f
            WeatherMode.RAIN -> it.rain
            WeatherMode.SNOW -> it.snow
        }
    }

    val selectedValue = values.getOrNull(selectedIndex) ?: 0f

    val accent = when (selectedMode) {
        WeatherMode.TEMP -> getTempColor(selectedValue)
        WeatherMode.WIND -> getWindColor(selectedValue)
        WeatherMode.RAIN -> getRainColor(selectedValue)
        WeatherMode.SNOW -> getSnowColor(selectedValue)
    }

    val description = getWeatherDescription(selectedMode, selectedValue)
    val animatedValue by animateFloatAsState(selectedValue)

    val pulse by rememberInfiniteTransition().animateFloat(
        10f, 22f,
        infiniteRepeatable(tween(800), RepeatMode.Reverse)
    )

    AppBackground {

        // ================= UI =================
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(state.backgroundColors)
                )

        ) {

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(Modifier.fillMaxWidth()) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                }

                Spacer(Modifier.height(10.dp))

                Text(day, style = MaterialTheme.typography.headlineLarge, color = Color.White)

                Spacer(Modifier.height(20.dp))

                GlassWeatherToggle(selectedMode, onModeChange)

                Spacer(Modifier.height(30.dp))

                // ================= GRAPH =================
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.08f))
                ) {

                    Box(
                        Modifier
                            .padding(20.dp)
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragEnd = { dragX = 0f }
                                ) { _, dragAmount ->
                                    dragX += dragAmount.x
                                }
                            }
                    ) {

                        Canvas(
                            Modifier
                                .fillMaxWidth()
                                .height(170.dp)
                        ) {

                            if (values.size < 2) return@Canvas

                            val max = values.maxOrNull() ?: 0f
                            val min = values.minOrNull() ?: 0f
                            val stepX = size.width / (values.size - 1)

                            val path = Path()
                            val points = mutableListOf<Offset>()

                            values.forEachIndexed { i, v ->
                                val x = i * stepX
                                val y = size.height - ((v - min) / (max - min + 0.1f)) * size.height

                                val p = Offset(x, y)
                                points.add(p)

                                if (i == 0) path.moveTo(p.x, p.y)
                                else path.lineTo(p.x, p.y)
                            }

                            drawPath(path, accent, style = Stroke(6f))

                            points.getOrNull(selectedIndex)?.let {
                                drawCircle(accent.copy(0.25f), pulse, it)
                                drawCircle(accent, 8f, it)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                val hour = hourly.getOrNull(selectedIndex)

                Text(hour?.time ?: "", color = Color.White.copy(0.6f))

                Text(
                    when (selectedMode) {
                        WeatherMode.TEMP -> "${animatedValue.toInt()}°"
                        WeatherMode.WIND -> stringResource(
                            R.string.wind_speed_format,
                            animatedValue.toInt().toString()
                        )

                        WeatherMode.RAIN -> stringResource(
                            R.string.rain_amount_format,
                            animatedValue.toInt().toString()
                        )

                        WeatherMode.SNOW -> stringResource(
                            R.string.snow_amount_format,
                            animatedValue.toInt().toString()
                        )
                    },
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(description, color = Color.White)

                Spacer(Modifier.height(30.dp))

                LazyRow(
                    state = listState,
                    flingBehavior = fling,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(horizontal = 40.dp)
                ) {

                    itemsIndexed(hourly) { index, hour ->

                        val selected = index == selectedIndex
                        val scale by animateFloatAsState(if (selected) 1.3f else 1f)

                        Card(
                            shape = RoundedCornerShape(22.dp),
                            colors = CardDefaults.cardColors(
                                if (selected) accent.copy(0.35f)
                                else Color.White.copy(0.1f)
                            ),
                            modifier = Modifier
                                .width(82.dp)
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                }
                        ) {

                            Column(
                                Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(hour.time, color = Color.White.copy(0.7f))
                                AsyncImage(hour.iconUrl, null, Modifier.size(30.dp))
                                Text(hour.temp, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
