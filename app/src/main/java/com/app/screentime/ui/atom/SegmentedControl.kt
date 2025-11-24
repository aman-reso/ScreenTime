@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.app.screentime.ui.atom

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.material3.MaterialTheme
import com.app.screentime.ui.theme.LocalAppColors
import com.app.screentime.ui.theme.LocalThemeMode
import com.app.screentime.ui.theme.ThemeType
import com.app.screentime.ui.theme.Typography
import com.app.screentime.ui.theme.getThemeColors

/**
 * SegmentedControl - A pill-style segmented control component
 * Replaces TabRow with a modern, pill-style design
 */

// Light Mode colors
private val LightModeBackground = Color(0xFFE0E7FF)  // Soft Indigo
private val LightModeSelectedBackground = Color(0xFF4338CA) // Medium Indigo
private val LightModeSelectedText = Color.White
private val LightModeUnselectedText = Color(0xFF4338CA) // Medium Indigo

// Dark Mode colors
private val DarkModeBackground = Color(0xFF4338CA)  // Medium Indigo
private val DarkModeSelectedBackground = Color(0xFF6366F1) // Lighter Indigo for selected
private val DarkModeSelectedText = Color.White
private val DarkModeUnselectedText = Color(0xFFC7D2FE) // Light Indigo for unselected

@Composable
fun SegmentedControl(
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkMode = LocalThemeMode.current
    
    // Select colors based on theme mode
    val segmentBackground = if (isDarkMode) DarkModeBackground else LightModeBackground
    val selectedBackground = if (isDarkMode) DarkModeSelectedBackground else LightModeSelectedBackground
    val selectedTextColor = if (isDarkMode) DarkModeSelectedText else LightModeSelectedText
    val unselectedTextColor = if (isDarkMode) DarkModeUnselectedText else LightModeUnselectedText
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(segmentBackground)
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedIndex == index

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(MaterialTheme.shapes.medium)
                        .background(
                            if (isSelected) selectedBackground
                            else Color.Transparent
                        )
                        .clickable { onItemSelected(index) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AppText(
                        text = item,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) selectedTextColor else unselectedTextColor
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SegmentedControlPreview() {
    SegmentedControlPreviewTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Two tabs preview
            var selectedIndex1 by remember { mutableStateOf(0) }
            SegmentedControl(
                items = listOf("Daily", "Weekly"),
                selectedIndex = selectedIndex1,
                onItemSelected = { selectedIndex1 = it }
            )

            // Three tabs preview
            var selectedIndex2 by remember { mutableStateOf(1) }
            SegmentedControl(
                items = listOf("All", "Active", "Completed"),
                selectedIndex = selectedIndex2,
                onItemSelected = { selectedIndex2 = it }
            )

            // Four tabs preview
            var selectedIndex3 by remember { mutableStateOf(2) }
            SegmentedControl(
                items = listOf("Tab 1", "Tab 2", "Tab 3", "Tab 4"),
                selectedIndex = selectedIndex3,
                onItemSelected = { selectedIndex3 = it }
            )
        }
    }
}

@Composable
private fun SegmentedControlPreviewTheme(content: @Composable () -> Unit) {
    val previewColors = remember { getThemeColors(ThemeType.CLASSIC_LIGHT) }
    CompositionLocalProvider(
        LocalThemeMode provides false,
        LocalAppColors provides previewColors
    ) {
        MaterialTheme(
            typography = Typography,
            content = content
        )
    }
}

