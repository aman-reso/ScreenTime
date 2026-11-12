package com.app.screentime.feature.newdesign.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Colors configuration for [WinterFilterChip].
 */
@Immutable
data class WinterFilterChipColors(
    val selectedContainerColor: Color,
    val unselectedContainerColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val selectedBorderColor: Color,
    val unselectedBorderColor: Color,
    val disabledSelectedContainerColor: Color = selectedContainerColor.copy(alpha = 0.5f),
    val disabledUnselectedContainerColor: Color = unselectedContainerColor.copy(alpha = 0.5f),
    val disabledSelectedContentColor: Color = selectedContentColor.copy(alpha = 0.5f),
    val disabledUnselectedContentColor: Color = unselectedContentColor.copy(alpha = 0.5f)
)

object WinterFilterChipDefaults {
    val Height: Dp = 33.dp
    val HorizontalPadding: Dp = 14.dp
    val VerticalPadding: Dp = 8.dp
    val CornerRadius: Dp = 100.dp
    val BorderWidth: Dp = 1.dp
    val FontSize = 12.sp

    // Figma Design Tokens (Node 62:810 "filter-active" & "filter-inactive")
    val SelectedContainerColor: Color @Composable @ReadOnlyComposable get() = WinterTokens.surfaceInverse
    val SelectedBorderColor: Color @Composable @ReadOnlyComposable get() = WinterTokens.surfaceInverse
    val SelectedContentColor: Color @Composable @ReadOnlyComposable get() = WinterTokens.neutral1000

    val UnselectedContainerColor: Color @Composable @ReadOnlyComposable get() = WinterTokens.surfaceRaised
    val UnselectedBorderColor: Color @Composable @ReadOnlyComposable get() = WinterTokens.borderSubtle
    val UnselectedContentColor: Color @Composable @ReadOnlyComposable get() = WinterTokens.textSecondary

    @Composable
    fun colors(
        selectedContainerColor: Color = WinterTokens.surfaceInverse,
        unselectedContainerColor: Color = WinterTokens.surfaceRaised,
        selectedContentColor: Color = WinterTokens.neutral1000,
        unselectedContentColor: Color = WinterTokens.textSecondary,
        selectedBorderColor: Color = WinterTokens.surfaceInverse,
        unselectedBorderColor: Color = WinterTokens.borderSubtle
    ): WinterFilterChipColors = WinterFilterChipColors(
        selectedContainerColor = selectedContainerColor,
        unselectedContainerColor = unselectedContainerColor,
        selectedContentColor = selectedContentColor,
        unselectedContentColor = unselectedContentColor,
        selectedBorderColor = selectedBorderColor,
        unselectedBorderColor = unselectedBorderColor
    )
}

/**
 * WinterFilterChip - Pure Jetpack Compose implementation of Figma "Luma / Filter" (Node 51:1634).
 *
 * Exact specifications from Figma Dev Mode:
 * - Height: 33dp
 * - Corner Radius: 100dp (Pill)
 * - Padding: 14dp horizontal, 8dp vertical
 * - Active State: Container #F2A4DA, Text #29162C, Border #F2A4DA
 * - Inactive State: Container #241A30, Text #BBAAC5, Border #48334F (1dp)
 * - Typography: 12sp, Medium weight, single-line
 *
 * @param text The text displayed inside the chip.
 * @param selected Whether the chip is currently active/selected.
 * @param onClick Callback invoked when the chip is tapped.
 * @param modifier The modifier to be applied to the chip.
 * @param enabled Whether the chip is clickable.
 * @param colors The [WinterFilterChipColors] used for this chip.
 */
@Composable
fun WinterFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: WinterFilterChipColors = WinterFilterChipDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val containerColor by animateColorAsState(
        targetValue = when {
            !enabled && selected -> colors.disabledSelectedContainerColor
            !enabled && !selected -> colors.disabledUnselectedContainerColor
            selected -> colors.selectedContainerColor
            else -> colors.unselectedContainerColor
        },
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "FilterChipContainerColor"
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            !enabled && selected -> colors.disabledSelectedContentColor
            !enabled && !selected -> colors.disabledUnselectedContentColor
            selected -> colors.selectedContentColor
            else -> colors.unselectedContentColor
        },
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "FilterChipContentColor"
    )

    val borderColor by animateColorAsState(
        targetValue = if (selected) colors.selectedBorderColor else colors.unselectedBorderColor,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "FilterChipBorderColor"
    )

    val shape = RoundedCornerShape(WinterFilterChipDefaults.CornerRadius)

    Box(
        modifier = modifier
            .height(WinterFilterChipDefaults.Height)
            .clip(shape)
            .background(containerColor, shape)
            .border(WinterFilterChipDefaults.BorderWidth, borderColor, shape)
            .clickable(
                enabled = enabled,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(
                horizontal = WinterFilterChipDefaults.HorizontalPadding,
                vertical = WinterFilterChipDefaults.VerticalPadding
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = WinterFilterChipDefaults.FontSize,
            fontWeight = FontWeight.Medium,
            lineHeight = 17.sp,
            maxLines = 1
        )
    }
}

/**
 * Horizontal scrollable row of [WinterFilterChip] items.
 */
@Composable
fun WinterFilterRow(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp)
) {
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement
    ) {
        items(items, key = { it }) { item ->
            WinterFilterChip(
                text = item,
                selected = item == selectedItem,
                onClick = { onItemSelected(item) }
            )
        }
    }
}

@Preview(name = "WinterFilterChip - States Preview", showBackground = true)
@Composable
private fun WinterFilterChipPreview() {
    var selectedFilter by remember { mutableStateOf("01 For you") }
    val sampleFilters = listOf(
        "01 For you",
        "02 Available now",
        "03 Following",
        "04 All activity",
        "05 Recharges",
        "06 Calls",
        "07 All messages",
        "08 Unread · 2",
        "09 Art & culture",
        "10 Travel",
        "11 Coffee"
    )

    Surface(color = WinterTokens.backgroundCanvas) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WinterFilterChip(
                text = "For you",
                selected = true,
                onClick = {}
            )
            WinterFilterChip(
                text = "Available now",
                selected = false,
                onClick = {}
            )
            WinterFilterChip(
                text = "Unread · 2",
                selected = false,
                onClick = {}
            )
        }
    }
}

