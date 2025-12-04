package com.telekom.odsystem.slots.quickactioncardpreferredcontent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-11-07 (v1.33.1) - uid: 1cd7f45c
 * Figma link: https://figma.com/design/Lv42UPNpBtiMLvZ33k8VHr/-ODS OneID Mobile Components?node-id=45713-2590
 */

class ODSQuickActionCardPreferredContentStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var copyAndSparklineGap: Dp? = null
    var copyAndSparklineVerticalAlignment: Alignment.Vertical? = null
    var copyAndSparklineHorizontalAlignment: Alignment.Horizontal? = null
    var copyAndSparklineVerticalArrangement: Arrangement.Vertical? = null
    var titleStyle: ODSTextStyle? = null
    var titleColor: HexColor? = null
    var titleTextAlign: TextAlign? = null
    var subtitleStyle: ODSTextStyle? = null
    var subtitleColor: HexColor? = null
    var subtitleTextAlign: TextAlign? = null
    var tagsContainerGap: Dp? = null
    var tagsContainerVerticalAlignment: Alignment.Vertical? = null
    var tagsContainerHorizontalAlignment: Alignment.Horizontal? = null
    var tagsContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var logoHeight: Dp? = null // Not exported from plugin
    fun getStyle(
        scheme: ODSTheme,
    ): ODSQuickActionCardPreferredContentStyle {
        val style = ODSQuickActionCardPreferredContentStyle()
        style.gap = DSQuickActionCardPreferredContentTokens.gap
        style.verticalAlignment = DSQuickActionCardPreferredContentTokens.verticalAlignment
        style.horizontalAlignment = DSQuickActionCardPreferredContentTokens.horizontalAlignment
        style.verticalArrangement = DSQuickActionCardPreferredContentTokens.verticalArrangement
        style.copyAndSparklineGap = DSQuickActionCardPreferredContentTokens.copyAndSparklineGap
        style.copyAndSparklineVerticalAlignment =
            DSQuickActionCardPreferredContentTokens.copyAndSparklineVerticalAlignment
        style.copyAndSparklineHorizontalAlignment =
            DSQuickActionCardPreferredContentTokens.copyAndSparklineHorizontalAlignment
        style.copyAndSparklineVerticalArrangement =
            DSQuickActionCardPreferredContentTokens.copyAndSparklineVerticalArrangement
        style.titleStyle = DSQuickActionCardPreferredContentTokens.titleStyle
        style.titleColor = scheme.basicText
        style.titleTextAlign = DSQuickActionCardPreferredContentTokens.titleTextAlign
        style.subtitleStyle = DSQuickActionCardPreferredContentTokens.subtitleStyle
        style.subtitleColor = scheme.basicText
        style.subtitleTextAlign = DSQuickActionCardPreferredContentTokens.subtitleTextAlign
        style.tagsContainerGap = DSQuickActionCardPreferredContentTokens.tagsContainerGap
        style.tagsContainerVerticalAlignment =
            DSQuickActionCardPreferredContentTokens.tagsContainerVerticalAlignment
        style.tagsContainerHorizontalAlignment =
            DSQuickActionCardPreferredContentTokens.tagsContainerHorizontalAlignment
        style.tagsContainerHorizontalArrangement =
            DSQuickActionCardPreferredContentTokens.tagsContainerHorizontalArrangement
        style.logoHeight = DSQuickActionCardPreferredContentTokens.logoHeight
        return style
    }
}
