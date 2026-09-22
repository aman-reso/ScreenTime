package com.telekom.odsystem.slots.choicecardcontent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-11-07 (v1.33.1) - uid: 2bf64b81
 * Figma link: https://figma.com/design/Lv42UPNpBtiMLvZ33k8VHr/-ODS OneID Mobile Components?node-id=45848-7317
 */

class ODSChoiceCardContentStyle {
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
    fun getStyle(
        scheme: ODSTheme,
    ): ODSChoiceCardContentStyle {
        val style = ODSChoiceCardContentStyle()
        style.gap = DSChoiceCardContentTokens.gap
        style.verticalAlignment = DSChoiceCardContentTokens.verticalAlignment
        style.horizontalAlignment = DSChoiceCardContentTokens.horizontalAlignment
        style.verticalArrangement = DSChoiceCardContentTokens.verticalArrangement
        style.headingStyle = DSChoiceCardContentTokens.headingStyle
        style.headingColor = scheme.basicText
        style.headingTextAlign = DSChoiceCardContentTokens.headingTextAlign
        style.bodyTextStyle = DSChoiceCardContentTokens.bodyTextStyle
        style.bodyTextColor = scheme.basicText
        style.bodyTextTextAlign = DSChoiceCardContentTokens.bodyTextTextAlign
        return style
    }
}
