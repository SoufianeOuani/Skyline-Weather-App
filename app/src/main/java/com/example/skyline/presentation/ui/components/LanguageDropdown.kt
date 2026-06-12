package com.example.skyline.presentation.ui.components

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LanguageDropdown(
    currentLang: String,
    activity: Activity,
    onLangChange: (String) -> Unit,
    glass: Brush
) {

    var expanded by remember { mutableStateOf(false) }

    val languages = listOf(
        "en" to "🇺🇸 English",
        "fr" to "🇫🇷 Français",
        "ar" to "🇲🇦 العربية",
        "es" to "🇪🇸 Español"
    )

    val currentLabel = languages.firstOrNull { it.first == currentLang }?.second ?: "EN"

    Box {

        // 🔥 BUTTON
        Row(
            modifier = Modifier
                .background(glass, RoundedCornerShape(50))
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.2f),
                    RoundedCornerShape(50)
                )
                .clickable { expanded = true }
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = currentLabel,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Color.White
            )
        }

        // 🔽 DROPDOWN
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(
                    Color(0xFF1E1E1E),
                    RoundedCornerShape(16.dp)
                )
        ) {

            languages.forEach { (code, label) ->

                val isSelected = code == currentLang

                DropdownMenuItem(
                    text = {
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else Color.White.copy(0.7f),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        expanded = false
                        if (!isSelected) {
                            onLangChange(code)
                        }
                    }
                )
            }
        }
    }
}