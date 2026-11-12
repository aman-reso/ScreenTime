package com.app.screentime.feature.newdesign.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * WinterSwitchColors defines the colors used by [WinterSwitch] in different states.
 */
@Immutable
data class WinterSwitchColors(
    val checkedTrackColor: Color,
    val uncheckedTrackColor: Color,
    val checkedThumbColor: Color,
    val uncheckedThumbColor: Color,
    val uncheckedBorderColor: Color = Color.Transparent,
    val disabledCheckedTrackColor: Color = checkedTrackColor.copy(alpha = 0.5f),
    val disabledUncheckedTrackColor: Color = uncheckedTrackColor.copy(alpha = 0.5f),
    val disabledCheckedThumbColor: Color = checkedThumbColor.copy(alpha = 0.7f),
    val disabledUncheckedThumbColor: Color = uncheckedThumbColor.copy(alpha = 0.7f)
)

object WinterSwitchDefaults {
    val TrackWidth: Dp = 48.dp
    val TrackHeight: Dp = 28.dp
    val TrackCornerRadius: Dp = 14.dp
    val ThumbSize: Dp = 22.dp
    val ThumbPadding: Dp = 3.dp

    // Design Tokens from Figma (Node 51:660 "Luma / Toggle")
    val CheckedTrackColor: Color @Composable @ReadOnlyComposable get() = WinterTokens.actionPrimary
    val UncheckedTrackColor: Color @Composable @ReadOnlyComposable get() = WinterTokens.surfaceCard
    val UncheckedBorderColor: Color @Composable @ReadOnlyComposable get() = WinterTokens.borderSubtle
    val ThumbColor: Color @Composable @ReadOnlyComposable get() = WinterTokens.controlThumb
    val UncheckedThumbColor: Color @Composable @ReadOnlyComposable get() = WinterTokens.systemHomeIndicator

    @Composable
    fun colors(
        checkedTrackColor: Color = WinterTokens.actionPrimary,
        uncheckedTrackColor: Color = WinterTokens.surfaceCard,
        checkedThumbColor: Color = WinterTokens.controlThumb,
        uncheckedThumbColor: Color = WinterTokens.systemHomeIndicator,
        uncheckedBorderColor: Color = WinterTokens.borderSubtle
    ): WinterSwitchColors = WinterSwitchColors(
        checkedTrackColor = checkedTrackColor,
        uncheckedTrackColor = uncheckedTrackColor,
        checkedThumbColor = checkedThumbColor,
        uncheckedThumbColor = uncheckedThumbColor,
        uncheckedBorderColor = uncheckedBorderColor
    )
}

/**
 * WinterSwitch - Pure Jetpack Compose implementation of Figma "Luma / Toggle" (Node 51:660).
 *
 * Exact specifications from Figma Dev Mode:
 * - Dimensions: 48dp (W) x 28dp (H)
 * - Corner Radius: 14dp (Full Pill capsule)
 * - Thumb: 22dp diameter circle, 3dp inset from all edges
 * - Checked Track Fill: #F2A4DA (Luma primary pink)
 * - Checked Thumb Fill: #FFFFFF (pure white)
 * - Animated sliding transition with smooth easing
 *
 * @param checked Whether the switch is currently ON (checked) or OFF (unchecked).
 * @param onCheckedChange Callback invoked when the switch state changes.
 * @param modifier The modifier to be applied to the switch.
 * @param enabled Whether the switch is enabled and interactive.
 * @param colors The [WinterSwitchColors] that will be used to resolve the colors.
 * @param interactionSource The [MutableInteractionSource] representing the stream of Interactions.
 */
@Composable
fun WinterSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: WinterSwitchColors = WinterSwitchDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val trackColor by animateColorAsState(
        targetValue = when {
            !enabled && checked -> colors.disabledCheckedTrackColor
            !enabled && !checked -> colors.disabledUncheckedTrackColor
            checked -> colors.checkedTrackColor
            else -> colors.uncheckedTrackColor
        },
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "WinterSwitchTrackColor"
    )

    val thumbColor by animateColorAsState(
        targetValue = when {
            !enabled && checked -> colors.disabledCheckedThumbColor
            !enabled && !checked -> colors.disabledUncheckedThumbColor
            checked -> colors.checkedThumbColor
            else -> colors.uncheckedThumbColor
        },
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "WinterSwitchThumbColor"
    )

    val maxOffset = WinterSwitchDefaults.TrackWidth - WinterSwitchDefaults.ThumbSize - WinterSwitchDefaults.ThumbPadding
    val minOffset = WinterSwitchDefaults.ThumbPadding

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) maxOffset else minOffset,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "WinterSwitchThumbOffset"
    )

    val toggleableModifier = if (onCheckedChange != null) {
        Modifier.toggleable(
            value = checked,
            enabled = enabled,
            role = Role.Switch,
            interactionSource = interactionSource,
            indication = null,
            onValueChange = onCheckedChange
        )
    } else {
        Modifier
    }

    val shape = RoundedCornerShape(WinterSwitchDefaults.TrackCornerRadius)

    Box(
        modifier = modifier
            .size(
                width = WinterSwitchDefaults.TrackWidth,
                height = WinterSwitchDefaults.TrackHeight
            )
            .then(toggleableModifier)
            .clip(shape)
            .background(trackColor, shape)
            .then(
                if (!checked && colors.uncheckedBorderColor != Color.Transparent) {
                    Modifier.border(1.dp, colors.uncheckedBorderColor, shape)
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(WinterSwitchDefaults.ThumbSize)
                .shadow(elevation = 2.dp, shape = CircleShape)
                .background(thumbColor, CircleShape)
        )
    }
}

@Preview(name = "WinterSwitch - Interactive Showcase", showBackground = true)
@Composable
private fun WinterSwitchShowcasePreview() {
    var isChecked by remember { mutableStateOf(true) }
    var isChecked2 by remember { mutableStateOf(false) }

    Surface(color = WinterTokens.backgroundCanvas) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            // Interactive Switch
            WinterSwitch(
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )

            // Switch OFF
            WinterSwitch(
                checked = isChecked2,
                onCheckedChange = { isChecked2 = it }
            )

            // Disabled ON
            WinterSwitch(
                checked = true,
                onCheckedChange = null,
                enabled = false
            )

            // Disabled OFF
            WinterSwitch(
                checked = false,
                onCheckedChange = null,
                enabled = false
            )
        }
    }
}
