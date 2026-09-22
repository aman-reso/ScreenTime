package com.telekom.odsystem.slots.featurelistitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSFeatureListItemStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var iconColor: HexColor? = null
    var iconWidth: Dp? = null
    var iconHeight: Dp? = null
    var labelStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSFeatureListItemStyle {
        val style = ODSFeatureListItemStyle()
        style.gap = DSFeatureListItemTokens.gap
        style.verticalAlignment = DSFeatureListItemTokens.verticalAlignment
        style.horizontalAlignment = DSFeatureListItemTokens.horizontalAlignment
        style.horizontalArrangement = DSFeatureListItemTokens.horizontalArrangement
        style.iconColor = scheme.basicTextRecessive
        style.iconWidth = DSFeatureListItemTokens.iconWidth
        style.iconHeight = DSFeatureListItemTokens.iconHeight
        style.labelStyle = DSFeatureListItemTokens.labelStyle
        style.labelColor = scheme.basicTextRecessive
        style.labelTextAlign = DSFeatureListItemTokens.labelTextAlign
        return style
    }
}
