package com.telekom.odsystem.atoms.carouseldot

import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSCarouselDotStyle {
    var borderRadius: ODSCorners? = null
    var border: Dp? = null
    var borderColor: List<ODSColorModel>? = null
    var width: Dp? = null
    var height: Dp? = null
    var clipContent: Boolean? = null
    var backgroundColor: List<ODSColorModel>? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSCarouselDotProps
    ): ODSCarouselDotStyle {
        var style = ODSCarouselDotStyle()
        style.borderRadius = DSCarouselDotTokens.borderRadius
        style.width = DSCarouselDotTokens.width
        style.height = DSCarouselDotTokens.height
        style.clipContent = DSCarouselDotTokens.clipContent
        if (props.selected) {
            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
        }
        if (!props.selected) {
            style.border = DSCarouselDotTokens.border
            style.borderColor = listOf(ODSColorModel(hexColor = scheme.basicTextRecessive))
        }
        return style
    }
}
