package com.telekom.odsystem.slots.productcarddescriptivetext

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSProductCardDescriptiveTextStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var headingStyle: ODSTextStyle? = null
    var headingColor: HexColor? = null
    var headingTextAlign: TextAlign? = null
    var bodyTextStyle: ODSTextStyle? = null
    var bodyTextColor: HexColor? = null
    var bodyTextTextAlign: TextAlign? = null
    var listContainerGap: Dp? = null
    var listContainerVerticalAlignment: Alignment.Vertical? = null
    var listContainerHorizontalAlignment: Alignment.Horizontal? = null
    var listContainerVerticalArrangement: Arrangement.Vertical? = null

    fun getStyle(
        scheme: ODSTheme
    ): ODSProductCardDescriptiveTextStyle {
        val style = ODSProductCardDescriptiveTextStyle()
        style.gap = DSProductCardDescriptiveTextTokens.gap
        style.verticalAlignment = DSProductCardDescriptiveTextTokens.verticalAlignment
        style.horizontalAlignment = DSProductCardDescriptiveTextTokens.horizontalAlignment
        style.verticalArrangement = DSProductCardDescriptiveTextTokens.verticalArrangement
        style.headingStyle = DSProductCardDescriptiveTextTokens.headingStyle
        style.headingColor = scheme.basicText
        style.headingTextAlign = DSProductCardDescriptiveTextTokens.headingTextAlign
        style.bodyTextStyle = DSProductCardDescriptiveTextTokens.bodyTextStyle
        style.bodyTextColor = scheme.basicTextRecessive
        style.bodyTextTextAlign = DSProductCardDescriptiveTextTokens.bodyTextTextAlign
        style.listContainerGap = DSProductCardDescriptiveTextTokens.listContainerGap
        style.listContainerVerticalAlignment =
            DSProductCardDescriptiveTextTokens.listContainerVerticalAlignment
        style.listContainerHorizontalAlignment =
            DSProductCardDescriptiveTextTokens.listContainerHorizontalAlignment
        style.listContainerVerticalArrangement =
            DSProductCardDescriptiveTextTokens.listContainerVerticalArrangement
        return style
    }
}
