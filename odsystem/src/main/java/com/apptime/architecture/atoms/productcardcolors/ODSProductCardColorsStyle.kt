package com.telekom.odsystem.atoms.productcardcolors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSProductCardColorsStyle {
    var gap: Dp? = null
    var padding: ODSPadding? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var listContainerGap: Dp? = null
    var listContainerVerticalAlignment: Alignment.Vertical? = null
    var listContainerHorizontalAlignment: Alignment.Horizontal? = null
    var listContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var overflowCountStyle: ODSTextStyle? = null
    var overflowCountColor: HexColor? = null
    var overflowCountTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSProductCardColorsStyle {
        val style = ODSProductCardColorsStyle()
        style.gap = DSProductCardColorsTokens.gap
        style.padding = DSProductCardColorsTokens.padding
        style.verticalAlignment = DSProductCardColorsTokens.verticalAlignment
        style.horizontalAlignment = DSProductCardColorsTokens.horizontalAlignment
        style.horizontalArrangement = DSProductCardColorsTokens.horizontalArrangement
        style.listContainerGap = DSProductCardColorsTokens.listContainerGap
        style.listContainerVerticalAlignment =
            DSProductCardColorsTokens.listContainerVerticalAlignment
        style.listContainerHorizontalAlignment =
            DSProductCardColorsTokens.listContainerHorizontalAlignment
        style.listContainerHorizontalArrangement =
            DSProductCardColorsTokens.listContainerHorizontalArrangement
        style.overflowCountStyle = DSProductCardColorsTokens.overflowCountStyle
        style.overflowCountColor = scheme.basicTextRecessive
        style.overflowCountTextAlign = DSProductCardColorsTokens.overflowCountTextAlign
        return style
    }
}
