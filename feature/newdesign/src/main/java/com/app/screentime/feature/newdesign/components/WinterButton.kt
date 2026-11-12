package com.app.screentime.feature.newdesign.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.screentime.feature.newdesign.R

/**
 * Button variants supported by [WinterButton] mapped to Figma tokens.
 */
enum class WinterButtonVariant {
    /** Primary CTA (Fill #F2A4DA, Text #29162C) */
    PRIMARY,

    /** Secondary / Raised Dark Surface (Fill #302139, Text #FFF7FF) */
    SECONDARY,

    /** Outlined Inverse White (Fill #FFF7FF, Border #C9A8E8, Text #29162C) */
    OUTLINED_WHITE
}

@Immutable
data class WinterButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val borderColor: Color = Color.Transparent,
    val disabledContainerColor: Color = containerColor.copy(alpha = 0.5f),
    val disabledContentColor: Color = contentColor.copy(alpha = 0.5f),
    val disabledBorderColor: Color = borderColor.copy(alpha = 0.5f)
)

object WinterButtonDefaults {
    val Height: Dp = 54.dp
    val CornerRadius: Dp = 28.dp
    val HorizontalPadding: Dp = 20.dp
    val FontSize = 15.sp

    // Figma Design Tokens (Node 51:1649 "Luma / Button")
    val PrimaryContainer = WinterTokens.Default.actionPrimary
    val PrimaryContent = WinterTokens.Default.textOnAccent
    val SecondaryContainer = WinterTokens.Default.surfaceRaised
    val SecondaryContent = WinterTokens.Default.textPrimary
    val OutlinedContainer = WinterTokens.Default.surfaceInverse
    val OutlinedContent = WinterTokens.Default.textOnAccent
    val OutlinedBorder = WinterTokens.Default.borderAccent

    @Composable
    fun colors(variant: WinterButtonVariant): WinterButtonColors {
        return when (variant) {
            WinterButtonVariant.PRIMARY -> WinterButtonColors(
                containerColor = WinterTokens.actionPrimary,
                contentColor = WinterTokens.textOnAccent
            )
            WinterButtonVariant.SECONDARY -> WinterButtonColors(
                containerColor = WinterTokens.surfaceRaised,
                contentColor = WinterTokens.textPrimary
            )
            WinterButtonVariant.OUTLINED_WHITE -> WinterButtonColors(
                containerColor = WinterTokens.surfaceInverse,
                contentColor = WinterTokens.textOnAccent,
                borderColor = WinterTokens.borderAccent
            )
        }
    }
}

/**
 * WinterButton - Pure Jetpack Compose implementation of Figma "Luma / Button" (Node 51:1649).
 *
 * Exact specifications from Figma Dev Mode:
 * - Height: 54dp
 * - Corner Radius: 28dp (Pill capsule)
 * - Padding: 20dp horizontal
 * - Typography: 15sp Inter, single-line
 * - Variants:
 *   - PRIMARY: #F2A4DA container, #29162C text
 *   - SECONDARY: #302139 container, #FFF7FF text
 *   - OUTLINED_WHITE: #FFF7FF container, #C9A8E8 border (1dp), #29162C text
 *
 * @param text The button label.
 * @param onClick Callback invoked when tapped.
 * @param modifier Applied to the button container.
 * @param variant Visual variant of the button.
 * @param enabled Whether the button is interactive.
 * @param leadingIcon Optional composable icon slot on the left.
 * @param trailingIcon Optional composable icon slot on the right.
 */
@Composable
fun WinterButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: WinterButtonVariant = WinterButtonVariant.PRIMARY,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val buttonColors = WinterButtonDefaults.colors(variant = variant)

    val containerColor by animateColorAsState(
        targetValue = if (enabled) buttonColors.containerColor else buttonColors.disabledContainerColor,
        animationSpec = tween(durationMillis = 150),
        label = "WinterButtonContainer"
    )

    val contentColor by animateColorAsState(
        targetValue = if (enabled) buttonColors.contentColor else buttonColors.disabledContentColor,
        animationSpec = tween(durationMillis = 150),
        label = "WinterButtonContent"
    )

    val borderColor by animateColorAsState(
        targetValue = if (enabled) buttonColors.borderColor else buttonColors.disabledBorderColor,
        animationSpec = tween(durationMillis = 150),
        label = "WinterButtonBorder"
    )

    val shape = RoundedCornerShape(WinterButtonDefaults.CornerRadius)

    Box(
        modifier = modifier
            .height(WinterButtonDefaults.Height)
            .clip(shape)
            .background(containerColor, shape)
            .then(
                if (buttonColors.borderColor != Color.Transparent) {
                    Modifier.border(1.dp, borderColor, shape)
                } else {
                    Modifier
                }
            )
            .clickable(
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = WinterButtonDefaults.HorizontalPadding),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            leadingIcon?.invoke()

            Text(
                text = text,
                color = contentColor,
                fontSize = WinterButtonDefaults.FontSize,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )

            trailingIcon?.invoke()
        }
    }
}

@Preview(name = "WinterButton - Showcase Preview", showBackground = true)
@Composable
private fun WinterButtonPreview() {
    Surface(color = WinterTokens.backgroundCanvas) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WinterButton(
                text = "Withdraw $56.00",
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                variant = WinterButtonVariant.PRIMARY
            )

            WinterButton(
                text = "Continue with Google",
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                variant = WinterButtonVariant.OUTLINED_WHITE,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google_mark),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            WinterButton(
                text = "Message Maya",
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                variant = WinterButtonVariant.SECONDARY
            )

            WinterButton(
                text = "Disabled Action",
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                variant = WinterButtonVariant.PRIMARY,
                enabled = false
            )
        }
    }
}

