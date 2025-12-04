package com.telekom.odsystem.slots.contentpanel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSTextStyle

/**
 * Code generated with ODS RADD Code Generator
 * 2025-08-04 (v1.32.3) - uid: 4801a273
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=16907-23850
 */

data class ODSContentPanelTokens(
    val gap: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val cardContentGap: Dp,
    val cardContentVerticalAlignment: Alignment.Vertical,
    val cardContentHorizontalAlignment: Alignment.Horizontal,
    val cardContentVerticalArrangement: Arrangement.Vertical,
    val contentContainerGap: Dp,
    val contentContainerVerticalAlignment: Alignment.Vertical,
    val contentContainerHorizontalAlignment: Alignment.Horizontal,
    val contentContainerVerticalArrangement: Arrangement.Vertical,
    val segmentTextStyle: ODSTextStyle,
    val segmentTextTextAlign: TextAlign,
    val segmentTextMinHeight: Dp,
    val actionButtonsVerticalAlignment: Alignment.Vertical,
    val actionButtonsHorizontalArrangement: Arrangement.Horizontal,
    val controlsVerticalAlignment: Alignment.Vertical,
    val controlsHorizontalAlignment: Alignment.Horizontal,
    val controlsHorizontalArrangement: Arrangement.Horizontal
)

val defaultODSContentPanelTokens = ODSContentPanelTokens(
    gap = DSVariables.spacingComponent8,
    verticalAlignment = Alignment.Bottom,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Bottom,
    cardContentGap = DSVariables.spacingComponent7,
    cardContentVerticalAlignment = Alignment.Top,
    cardContentHorizontalAlignment = Alignment.Start,
    cardContentVerticalArrangement = Arrangement.Top,
    contentContainerGap = DSVariables.spacingComponent3,
    contentContainerVerticalAlignment = Alignment.Top,
    contentContainerHorizontalAlignment = Alignment.Start,
    contentContainerVerticalArrangement = Arrangement.Top,
    segmentTextStyle = DSTextStyles.bodyMBold,
    segmentTextTextAlign = TextAlign.Left,
    segmentTextMinHeight = DSVariables.sizingComponent13,
    actionButtonsVerticalAlignment = Alignment.Bottom,
    actionButtonsHorizontalArrangement = Arrangement.SpaceBetween,
    controlsVerticalAlignment = Alignment.CenterVertically,
    controlsHorizontalAlignment = Alignment.Start,
    controlsHorizontalArrangement = Arrangement.Start
)

var DSContentPanelTokens: ODSContentPanelTokens = defaultODSContentPanelTokens
