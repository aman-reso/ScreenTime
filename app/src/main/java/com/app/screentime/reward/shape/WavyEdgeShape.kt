package com.app.screentime.reward.shape

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.sin

/**
 * Custom shape with wavy/scalloped edges on right side only
 * Creates a coupon/ticket-like appearance with semicircular cutouts
 */

class WavyShape(
    private val amplitude: Float = 20f,  // How far the waves extend
    private val frequency: Float = 0.02f  // How many waves (lower = fewer, smoother waves)
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(Path().apply {
            val width = size.width
            val height = size.height

            // Start at top-left corner
            moveTo(0f, 0f)

            // Draw straight line to top-right (minus amplitude to leave room for waves)
            lineTo(width - amplitude, 0f)

            for (i in 0..height.toInt()) {
                val y = i.toFloat()
                val x = width - amplitude + (amplitude * sin(y * frequency))
                lineTo(x, y)
            }

            // Draw straight line along the bottom
            lineTo(0f, height)

            // Close the path (automatically draws left edge)
            close()
        })
    }
}
