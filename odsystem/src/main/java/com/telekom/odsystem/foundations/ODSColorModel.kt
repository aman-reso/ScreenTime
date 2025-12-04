package com.telekom.odsystem.foundations

import androidx.compose.ui.graphics.Brush

class ODSColorModel {
    var hexColor: HexColor? = null
    var brush: Brush? = null

    constructor(hexColor: HexColor) {
        this.hexColor = hexColor
    }

    constructor(brush: Brush) {
        this.brush = brush
    }

    constructor(gradient: ODSLinearGradientModel) {
        this.brush = Brush.linearGradient(gradient)
    }
}

class ODSLinearGradientModel(
    vararg val colorStops: Pair<Float, HexColor>,
    var angleInDegrees: Float = 0f,
    var opacity: Float = 1f,
)
