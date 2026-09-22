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
        spread.toFloat()
        x.toFloat()
        y.toFloat()
        color.getColor()
        // todo add elevation overlay
        return shadowRadius
    }

    fun toElevationValue(): Float {
        val shadowRadius = blur.toFloat()
        spread.toFloat()
        x.toFloat()
        y.toFloat()
        color.getColor()
        // todo add elevation overlay
        return shadowRadius
    }
}

@Serializable
enum class ODSElevationType {
    DROP_SHADOW,
    INNER_SHADOW
}
