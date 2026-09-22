package com.telekom.odsystem.foundations

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Created by dmarinopoulos on 6/2/24
 */

@Composable
fun Modifier.underline(thickness: Dp, color: Color, padding: Dp = 0.dp) = this.then(
    Modifier.drawWithContent {
        val thicknessPx = with(density) { thickness.toPx() }
        val paddingPx = with(density) { padding.toPx() }

        drawContent()

        val yOffset = size.height - thicknessPx / 2 + paddingPx

        drawLine(
            color = color,
            start = Offset(x = 0f, y = yOffset),
            end = Offset(x = size.width, y = yOffset),
            strokeWidth = thicknessPx
        )
    }
)
