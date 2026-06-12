package com.example.skyline.presentation.ui.components

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skyline.R
import com.example.skyline.data.remote.dto.SearchResponse
import com.example.skyline.presentation.ui.screens.home.HomeState

@SuppressLint("ContextCastToActivity")
@Composable
fun TopBarSection(
    state: HomeState,
    onQueryChange: (String) -> Unit,
    onCitySelected: (SearchResponse) -> Unit,
    onClearFocus: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager: FocusManager = LocalFocusManager.current
    val activity = LocalContext.current as Activity
    val density = LocalDensity.current

    val glassBrush = Brush.verticalGradient(
        listOf(
            Color.White.copy(alpha = 0.12f),
            Color.White.copy(alpha = 0.04f)
        )
    )

    var expanded by remember { mutableStateOf(false) }
    var showRestartDialog by remember { mutableStateOf(false) }
    var selectedLang by remember { mutableStateOf("") }
    var anchorWidth by remember { mutableStateOf(0.dp) }

    val languages = listOf(
        "en" to "🇺🇸",
        "fr" to "🇫🇷",
        "ar" to "🇲🇦",
        "es" to "🇪🇸"
    )

    val currentLabel = languages.firstOrNull { it.first == state.currentLanguage }?.second ?: "🌐"

    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    // 🌐 LANGUAGE SELECTOR
                    Box(
                        modifier = Modifier.onGloballyPositioned { coordinates ->
                            anchorWidth = with(density) { coordinates.size.width.toDp() }
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(glassBrush)
                                .border(
                                    0.5.dp,
                                    Brush.verticalGradient(listOf(Color.White.copy(0.2f), Color.Transparent)),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { expanded = true }
                                .padding(horizontal = 12.dp, vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = currentLabel, fontSize = 18.sp, color = Color.White)
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .width(anchorWidth)
                                .background(Color(0xFF1A1A1A), RoundedCornerShape(12.dp))
                        ) {
                            languages.forEach { (code, label) ->
                                val isSelected = code == state.currentLanguage
                                DropdownMenuItem(
                                    contentPadding = PaddingValues(0.dp),
                                    text = {
                                        Text(
                                            text = label,
                                            color = if (isSelected) Color.White else Color.White.copy(0.6f),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                        if (code != state.currentLanguage) {
                                            selectedLang = code
                                            showRestartDialog = true
                                        }
                                    }
                                )
                            }
                        }
                    }

                    // 🔍 SEARCH BAR (With City Tag inside)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(glassBrush)
                            .border(
                                0.5.dp,
                                Brush.verticalGradient(listOf(Color.White.copy(0.2f), Color.Transparent)),
                                RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = state.searchQuery,
                                onValueChange = onQueryChange,
                                modifier = Modifier.weight(1f),
                                placeholder = {
                                    Text(
                                        stringResource(R.string.search_hint),
                                        color = Color.White.copy(alpha = 0.4f),
                                        fontSize = 15.sp
                                    )
                                },
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    cursorColor = Color.White,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                trailingIcon = {
                                    if (state.searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { onQueryChange("") }) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = null,
                                                tint = Color.White.copy(alpha = 0.5f),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            )

                            // 📍 Current City Badge (Shown when not searching)
                            if (state.city.isNotEmpty() && state.searchQuery.isEmpty()) {
                                Surface(
                                    modifier = Modifier
                                        .padding(end = 12.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    color = Color.White.copy(alpha = 0.1f)
                                ) {
                                    Text(
                                        text = state.city,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }

                // 🔎 SEARCH RESULTS
                if (state.suggestions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212).copy(alpha = 0.95f))
                    ) {
                        LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                            items(state.suggestions) { city ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            focusManager.clearFocus()
                                            onCitySelected(city)
                                        }
                                        .padding(horizontal = 20.dp, vertical = 16.dp)
                                ) {
                                    Text(city.name, color = Color.White, fontWeight = FontWeight.Bold)
                                    city.region?.let {
                                        Text(it, color = Color.White.copy(alpha = 0.5f), style = MaterialTheme.typography.labelMedium)
                                    }
                                }
                                HorizontalDivider(color = Color.White.copy(alpha = 0.06f))
                            }
                        }
                    }
                }
            }
            HorizontalDivider(color = Color.White.copy(alpha = 0.1f), thickness = 0.5.dp)
        }
    }

    if (showRestartDialog) {
        AlertDialog(
            onDismissRequest = { showRestartDialog = false },
            title = { Text("Restart Required") },
            text = { Text("Restart the app to apply the new language?") },
            confirmButton = {
                TextButton(onClick = {
                    showRestartDialog = false
                    changeLang(activity, selectedLang)
                    activity.finish()
                    activity.startActivity(activity.intent)
                }) { Text("Restart", fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showRestartDialog = false }) { Text("Stay") }
            }
        )
    }
}

fun changeLang(context: Context, lang: String) {
    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    prefs.edit().putString("lang", lang).apply()
}