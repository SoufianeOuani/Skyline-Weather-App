package com.example.skyline.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.skyline.data.remote.dto.FavoriteCity

@Composable
fun FavoriteCitiesSection(
    favorites: List<FavoriteCity>,
    onCityClick: (String) -> Unit,
    onRemove: (String) -> Unit
) {
    if (favorites.isEmpty()) return

    Column(modifier = Modifier.fillMaxWidth()) {
        // 🔹 Section Title with improved spacing
        Text(
            text = "Favorite Locations",
            color = Color.White.copy(alpha = 0.9f),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(favorites) { city ->

                // 🌈 Multi-layered Glass Brushes
                val cardGlass = Brush.verticalGradient(
                    listOf(Color.White.copy(0.15f), Color.White.copy(0.02f))
                )

                val borderGlass = Brush.verticalGradient(
                    listOf(Color.White.copy(0.3f), Color.Transparent, Color.White.copy(0.1f))
                )

                Box(
                    modifier = Modifier
                        .width(145.dp)
                        .height(175.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(cardGlass)
                        .border(1.dp, borderGlass, RoundedCornerShape(28.dp))
                        .clickable { onCityClick(city.name) }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // 🌤️ Weather icon with a soft shadow effect
                        AsyncImage(
                            model = city.iconUrl,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // 🌍 City Name
                        Text(
                            text = city.name,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )

                        // 🔥 Temperature Badge (Modern pill look)
                        Surface(
                            modifier = Modifier.padding(top = 6.dp),
                            color = Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = city.temp,
                                color = Color.White.copy(alpha = 0.9f),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}