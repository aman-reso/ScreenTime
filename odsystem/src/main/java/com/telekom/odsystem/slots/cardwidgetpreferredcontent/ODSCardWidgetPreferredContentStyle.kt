package com.telekom.odsystem.slots.cardwidgetpreferredcontent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-10-10 (v1.33.1) - uid: aac0bf
 * Figma link: https://figma.com/design/hre4oCiCoYfStczE6GmCaA/Untitled?node-id=141-12150
 */

class ODSCardWidgetPreferredContentStyle {
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var titleStyle: ODSTextStyle? = null
    var titleColor: HexColor? = null
    var titleTextAlign: TextAlign? = null
    var subtitleStyle: ODSTextStyle? = null
    var subtitleColor: HexColor? = null
    var subtitleTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme,
    ): ODSCardWidgetPreferredContentStyle {
        val style = ODSCardWidgetPreferredContentStyle()
        style.verticalAlignment = DSCardWidgetPreferredContentTokens.verticalAlignment
        style.horizontalAlignment = DSCardWidgetPreferredContentTokens.horizontalAlignment
        style.verticalArrangement = DSCardWidgetPreferredContentTokens.verticalArrangement
        style.titleStyle = DSCardWidgetPreferredContentTokens.titleStyle
        style.titleColor = scheme.basicText
        style.titleTextAlign = DSCardWidgetPreferredContentTokens.titleTextAlign
        style.subtitleStyle = DSCardWidgetPreferredContentTokens.subtitleStyle
        style.subtitleColor = scheme.basicTextRecessive
        style.subtitleTextAlign = DSCardWidgetPreferredContentTokens.subtitleTextAlign
        return style
    }
}
