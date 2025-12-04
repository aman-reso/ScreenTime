package com.telekom.odsystem.slots.cardfeaturepreferredcontent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-25 (v1.33.1) - uid: 506748f4
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=9079-18873
 */

class ODSCardFeaturePreferredContentStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var productNameStyle: ODSTextStyle? = null
    var productNameColor: HexColor? = null
    var productNameTextAlign: TextAlign? = null
    var productPriceStyle: ODSTextStyle? = null
    var productPriceColor: HexColor? = null
    var productPriceTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme,
    ): ODSCardFeaturePreferredContentStyle {
        val style = ODSCardFeaturePreferredContentStyle()
        style.gap = DSCardFeaturePreferredContentTokens.gap
        style.verticalAlignment = DSCardFeaturePreferredContentTokens.verticalAlignment
        style.horizontalAlignment = DSCardFeaturePreferredContentTokens.horizontalAlignment
        style.verticalArrangement = DSCardFeaturePreferredContentTokens.verticalArrangement
        style.productNameStyle = DSCardFeaturePreferredContentTokens.productNameStyle
        style.productNameColor = scheme.basicText
        style.productNameTextAlign = DSCardFeaturePreferredContentTokens.productNameTextAlign
        style.productPriceStyle = DSCardFeaturePreferredContentTokens.productPriceStyle
        style.productPriceColor = scheme.basicTextRecessive
        style.productPriceTextAlign = DSCardFeaturePreferredContentTokens.productPriceTextAlign
        return style
    }
}
