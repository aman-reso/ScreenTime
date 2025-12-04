package com.telekom.odsystem.slots.bottomsheetheader

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-04 (v1.32.3) - uid: 5c52583b
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=17924-44
 */

data class ODSBottomSheetHeaderTokens(
    val padding: ODSPadding,
    val minHeight: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val textContainerPaddingSizeLarge: ODSPadding,
    val textContainerPaddingSizeSmall: ODSPadding,
    val textContainerVerticalAlignment: Alignment.Vertical,
    val textContainerHorizontalAlignmentSizeLarge: Alignment.Horizontal,
    val textContainerHorizontalAlignmentSizeSmall: Alignment.Horizontal,
    val textContainerHorizontalArrangementSizeLarge: Arrangement.Horizontal,
    val textContainerGapSizeSmall: Dp,
    val textContainerVerticalArrangementSizeSmall: Arrangement.Vertical,
    val titleLabelStyleSizeLarge: ODSTextStyle,
    val titleLabelStyleSizeSmall: ODSTextStyle,
    val titleLabelTextAlign: TextAlign,
    val subtitleLabelStyleSizeSmall: ODSTextStyle,
    val subtitleLabelTextAlignSizeSmall: TextAlign
)

val defaultODSBottomSheetHeaderTokens = ODSBottomSheetHeaderTokens(
    padding = ODSPadding(top = DSVariables.spacingComponent2, left = DSVariables.spacingComponent7),
    minHeight = 56.dp,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start,
    textContainerPaddingSizeLarge = ODSPadding(top = DSVariables.spacingComponent3),
    textContainerPaddingSizeSmall = ODSPadding(top = DSVariables.spacingComponent4),
    textContainerVerticalAlignment = Alignment.CenterVertically,
    textContainerHorizontalAlignmentSizeLarge = Alignment.CenterHorizontally,
    textContainerHorizontalAlignmentSizeSmall = Alignment.Start,
    textContainerHorizontalArrangementSizeLarge = Arrangement.Center,
    textContainerGapSizeSmall = DSVariables.spacingComponent1,
    textContainerVerticalArrangementSizeSmall = Arrangement.Center,
    titleLabelStyleSizeLarge = DSTextStyles.titleL,
    titleLabelStyleSizeSmall = DSTextStyles.titleS,
    titleLabelTextAlign = TextAlign.Left,
    subtitleLabelStyleSizeSmall = DSTextStyles.bodyMRegular,
    subtitleLabelTextAlignSizeSmall = TextAlign.Left
)

var DSBottomSheetHeaderTokens: ODSBottomSheetHeaderTokens = defaultODSBottomSheetHeaderTokens
