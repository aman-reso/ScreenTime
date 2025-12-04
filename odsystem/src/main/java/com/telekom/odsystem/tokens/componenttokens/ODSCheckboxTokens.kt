package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

/**
 * Code generated with ODS RADD Code Generator Plugin
 * 2025-06-16 (v1.31.6) - uid: 4592c67e
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=15509-8039
 */

data class ODSCheckboxTokens(
    val gapSizeLarge: Dp,
    val gapSizeSmall: Dp,
    val paddingSizeLarge: ODSPadding,
    val paddingSizeSmall: ODSPadding,
    val minHeight: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val labelMessageGap: Dp,
    val labelMessagePaddingSizeLarge: ODSPadding,
    val labelMessagePaddingSizeSmall: ODSPadding,
    val labelMessageVerticalAlignment: Alignment.Vertical,
    val labelMessageHorizontalAlignment: Alignment.Horizontal,
    val labelMessageVerticalArrangement: Arrangement.Vertical,
    val labelStyleSizeLarge: ODSTextStyle,
    val labelStyleSizeSmall: ODSTextStyle,
    val labelTextAlign: TextAlign
)

val defaultODSCheckboxTokens = ODSCheckboxTokens(
    gapSizeLarge = DSVariables.spacingComponent5,
    gapSizeSmall = DSVariables.spacingComponent3,
    paddingSizeLarge = ODSPadding(
        top = DSVariables.spacingComponent3,
        bottom = DSVariables.spacingComponent3
    ),
    paddingSizeSmall = ODSPadding(
        top = DSVariables.spacingComponent4,
        bottom = DSVariables.spacingComponent4
    ),
    minHeight = DSVariables.sizingMinimumTappableArea,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start,
    labelMessageGap = DSVariables.spacingComponent2,
    labelMessagePaddingSizeLarge = ODSPadding(
        top = DSVariables.spacingComponent2,
        bottom = DSVariables.spacingComponent2
    ),
    labelMessagePaddingSizeSmall = ODSPadding(
        top = DSVariables.spacingComponent1,
        bottom = DSVariables.spacingComponent1
    ),
    labelMessageVerticalAlignment = Alignment.Top,
    labelMessageHorizontalAlignment = Alignment.Start,
    labelMessageVerticalArrangement = Arrangement.Top,
    labelStyleSizeLarge = DSTextStyles.subtitle,
    labelStyleSizeSmall = DSTextStyles.bodyMBold,
    labelTextAlign = TextAlign.Left
)

var DSCheckboxTokens: ODSCheckboxTokens = defaultODSCheckboxTokens
