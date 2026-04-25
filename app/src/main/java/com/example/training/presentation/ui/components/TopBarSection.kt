package com.example.training.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.training.data.remote.dto.SearchResponse
import com.example.training.presentation.ui.screens.home.HomeState

@Composable
fun TopBarSection(
    state: HomeState,
    onQueryChange: (String) -> Unit,
    onCitySelected: (SearchResponse) -> Unit,
    onClearFocus: () -> Unit, // 🔥 NEW
    modifier: Modifier = Modifier
) {

    val focusManager: FocusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        // =============================
        // 🔍 SEARCH FIELD
        // =============================
        TextField(
            value = state.searchQuery,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = {
                Text(
                    text = "Search city...",
                    color = Color.White.copy(alpha = 0.6f)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White.copy(alpha = 0.12f),
                focusedContainerColor = Color.White.copy(alpha = 0.18f),
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                if (state.searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = Color.White
                        )
                    }
                }
            }
        )

        // 🔥 SPACE BETWEEN SEARCH & CONTENT
        Spacer(modifier = Modifier.height(12.dp))

        // =============================
        // 📍 SIMPLE CLEAN DROPDOWN
        // =============================
        if (state.suggestions.isNotEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.2f), // 🔥 SIMPLE CONTRAST
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        1.dp,
                        Color.White.copy(alpha = 0.1f),
                        RoundedCornerShape(16.dp)
                    )
            ) {

                LazyColumn(
                    modifier = Modifier
                        .heightIn(max = 250.dp)
                ) {

                    items(state.suggestions) { city ->

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    focusManager.clearFocus()
                                    onCitySelected(city)
                                }
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 14.dp
                                )
                        ) {

                            Text(
                                text = city.name,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            city.region?.let {
                                Text(
                                    text = it,
                                    color = Color.White.copy(alpha = 0.6f),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Divider(
                            color = Color.White.copy(alpha = 0.08f),
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        }
    }
}