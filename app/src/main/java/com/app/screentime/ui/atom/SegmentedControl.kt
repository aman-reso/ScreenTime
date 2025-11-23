package com.app.screentime.ui.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.screentime.ui.theme.LocalAppColors

/**
 * SegmentedControl - A pill-style segmented control component
 * Replaces TabRow with a modern, pill-style design
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegmentedControl(
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current ?: return

    // Create a gradient-like background color (reddish-purple translucent)
    val backgroundColor = colors.success.copy(alpha = 0.2f).let { baseColor ->
        Color(
            red = (baseColor.red * 255 + 20).coerceAtMost(255f) / 255f,
            green = (baseColor.green * 255 - 10).coerceAtLeast(0f) / 255f,
            blue = (baseColor.blue * 255 + 30).coerceAtMost(255f) / 255f,
            alpha = 0.25f
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(backgroundColor)
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedIndex == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            if (isSelected) {
                                colors.card // White/light background for selected
                            } else {
                                Color.Transparent
                            }
                        )
                        .clickable { onItemSelected(index) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AppText(
                        text = item,
                        style = AppTextStyle.Body,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) {
                            colors.textPrimary // Dark text for selected (white background)
                        } else {
                            colors.textOnPrimary.copy(alpha = 0.95f) // White/light text for unselected
                        }
                    )
                }
            }
        }
    }
}

