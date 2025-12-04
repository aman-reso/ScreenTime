package com.telekom.odsystem.foundations

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable

@Serializable
@Suppress("MagicNumber")
data class ODSElevation(
    var x: Int = 0,
    var y: Int = 1,
    var blur: Int = 2,
    var spread: Int = 0,
    var color: HexColor = HexColor("#000000", 0.3f),
    var type: ODSElevationType = ODSElevationType.DROP_SHADOW
) {
    @Composable
    fun toElevation(): Dp {
        val shadowRadius = blur.dp
        val shadowSpread = spread.toFloat()
        val shadowOffsetX = x.toFloat()
        val shadowOffsetY = y.toFloat()
        val shadowColor = color.getColor()
        // todo add elevation overlay
        return shadowRadius
    }

    fun toElevationValue(): Float {
        val shadowRadius = blur.toFloat()
        val shadowSpread = spread.toFloat()
        val shadowOffsetX = x.toFloat()
        val shadowOffsetY = y.toFloat()
        val shadowColor = color.getColor()
        // todo add elevation overlay
        return shadowRadius
    }
}

@Serializable
enum class ODSElevationType {
    DROP_SHADOW,
    INNER_SHADOW
}
