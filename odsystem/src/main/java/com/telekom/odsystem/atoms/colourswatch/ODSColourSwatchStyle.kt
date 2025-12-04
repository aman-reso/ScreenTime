package com.telekom.odsystem.atoms.colourswatch

import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSColourSwatchStyle {
    var cornerRadius: ODSCorners? = null
    var border: Dp? = null
    var borderColor: List<ODSColorModel>? = null
    var width: Dp? = null
    var height: Dp? = null
    var clipContent: Boolean? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSColourSwatchStyle {
        val style = ODSColourSwatchStyle()
        style.cornerRadius = DSColourSwatchTokens.cornerRadius
        style.border = DSColourSwatchTokens.border
        style.borderColor = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        style.width = DSColourSwatchTokens.width
        style.height = DSColourSwatchTokens.height
        style.clipContent = DSColourSwatchTokens.clipContent
        return style
    }
}
