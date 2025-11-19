package com.app.screentime.ui.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.screentime.ui.theme.LocalAppColors


@Composable
fun AppGlassyCard(
    modifier: Modifier = Modifier,
    showBorder: Boolean = true,
    content: @Composable () -> Unit = {}
) {
    val colors = LocalAppColors.current ?: return
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(10.dp))
            .background(colors.card)
            .then(
                if (showBorder) {
                    Modifier.border(
                        1.dp,
                        Brush.linearGradient(
                            listOf(
                                colors.textPrimary.copy(alpha = 0.45f),
                                colors.textPrimary.copy(alpha = 0.05f)
                            )
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                } else Modifier
            )
    ) {
        content()
    }
}

private fun createGlassGradient(baseColor: Color, alpha: Float): Brush {
    return Brush.verticalGradient(
        colors = listOf(
            baseColor.copy(alpha = alpha * 1.1f),
            baseColor.copy(alpha = alpha),
            baseColor.copy(alpha = alpha)
        )
    )
}

private fun createGlassBorderGradient(borderColor: Color, alpha: Float): Brush {
    return Brush.verticalGradient(
        colors = listOf(
            borderColor.copy(alpha = alpha * 1.5f),  // Strong highlight
            borderColor.copy(alpha = alpha * 0.3f),  // Fade in middle
            borderColor.copy(alpha = alpha * 0.8f)   // Subtle bottom
        )
    )
}

// Removed hardcoded bgColor - use LocalAppColors.current.background instead
