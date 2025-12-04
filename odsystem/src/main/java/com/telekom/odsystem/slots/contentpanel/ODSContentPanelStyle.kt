package com.telekom.odsystem.slots.contentpanel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-08-04 (v1.32.3) - uid: 4801a273
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=16907-23850
 */

class ODSContentPanelStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var cardContentGap: Dp? = null
    var cardContentVerticalAlignment: Alignment.Vertical? = null
    var cardContentHorizontalAlignment: Alignment.Horizontal? = null
    var cardContentVerticalArrangement: Arrangement.Vertical? = null
    var contentContainerGap: Dp? = null
    var contentContainerVerticalAlignment: Alignment.Vertical? = null
    var contentContainerHorizontalAlignment: Alignment.Horizontal? = null
    var contentContainerVerticalArrangement: Arrangement.Vertical? = null
    var segmentTextStyle: ODSTextStyle? = null
    var segmentTextColor: HexColor? = null
    var segmentTextTextAlign: TextAlign? = null
    var segmentTextMinHeight: Dp? = null
    var actionButtonsVerticalAlignment: Alignment.Vertical? = null
    var actionButtonsHorizontalArrangement: Arrangement.Horizontal? = null
    var controlsVerticalAlignment: Alignment.Vertical? = null
    var controlsHorizontalAlignment: Alignment.Horizontal? = null
    var controlsHorizontalArrangement: Arrangement.Horizontal? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSContentPanelStyle {
        val style = ODSContentPanelStyle()
        style.gap = DSContentPanelTokens.gap
        style.verticalAlignment = DSContentPanelTokens.verticalAlignment
        style.horizontalAlignment = DSContentPanelTokens.horizontalAlignment
        style.verticalArrangement = DSContentPanelTokens.verticalArrangement
        style.cardContentGap = DSContentPanelTokens.cardContentGap
        style.cardContentVerticalAlignment = DSContentPanelTokens.cardContentVerticalAlignment
        style.cardContentHorizontalAlignment = DSContentPanelTokens.cardContentHorizontalAlignment
        style.cardContentVerticalArrangement = DSContentPanelTokens.cardContentVerticalArrangement
        style.contentContainerGap = DSContentPanelTokens.contentContainerGap
        style.contentContainerVerticalAlignment =
            DSContentPanelTokens.contentContainerVerticalAlignment
        style.contentContainerHorizontalAlignment =
            DSContentPanelTokens.contentContainerHorizontalAlignment
        style.contentContainerVerticalArrangement =
            DSContentPanelTokens.contentContainerVerticalArrangement
        style.segmentTextStyle = DSContentPanelTokens.segmentTextStyle
        style.segmentTextColor = scheme.basicText
        style.segmentTextTextAlign = DSContentPanelTokens.segmentTextTextAlign
        style.segmentTextMinHeight = DSContentPanelTokens.segmentTextMinHeight
        style.actionButtonsVerticalAlignment = DSContentPanelTokens.actionButtonsVerticalAlignment
        style.actionButtonsHorizontalArrangement =
            DSContentPanelTokens.actionButtonsHorizontalArrangement
        style.controlsVerticalAlignment = DSContentPanelTokens.controlsVerticalAlignment
        style.controlsHorizontalAlignment = DSContentPanelTokens.controlsHorizontalAlignment
        style.controlsHorizontalArrangement = DSContentPanelTokens.controlsHorizontalArrangement
        return style
    }
}
