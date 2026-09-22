package com.telekom.odsystem.molecules.segmentedbutton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSSegmentedButtonStyle {
    var gap: Dp? = null
    var backgroundColor: List<ODSColorModel>? = null
    var padding: ODSPadding? = null
    var borderRadius: ODSCorners? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSSegmentedButtonProps
    ): ODSSegmentedButtonStyle {
        val style = ODSSegmentedButtonStyle()
        style.gap = DSSegmentedButtonTokens.gap
        style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
        style.borderRadius = DSSegmentedButtonTokens.borderRadius
        style.verticalAlignment = DSSegmentedButtonTokens.verticalAlignment
        style.horizontalAlignment = DSSegmentedButtonTokens.horizontalAlignment
        style.horizontalArrangement = DSSegmentedButtonTokens.horizontalArrangement
        if (props.size == ODSSegmentedButtonSize.LARGE) {
            style.padding = DSSegmentedButtonTokens.paddingSizeLarge
        }
        if (props.size == ODSSegmentedButtonSize.SMALL) {
            style.padding = DSSegmentedButtonTokens.paddingSizeSmall
        }
        return style
    }
}
