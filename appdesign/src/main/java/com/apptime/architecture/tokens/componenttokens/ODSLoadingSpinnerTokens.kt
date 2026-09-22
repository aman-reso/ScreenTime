package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSTextStyle

/**
 * Code generated with ODS RADD Code Generator Plugin
 * 2025-06-16 (v1.31.6) - uid: 1932e655
 * Figma link: https://figma.com/design/MpQgyLR8JN6QeprILJwaD4/ODS_Feedback-Components_Exploration?node-id=866-16068
 */

data class ODSLoadingSpinnerTokens(
    val gapLabelAlignmentNone: Dp,
    val gapLabelAlignmentVertical: Dp,
    val gapLabelAlignmentHorizontal: Dp,
    val verticalAlignmentLabelAlignmentNone: Alignment.Vertical,
    val verticalAlignmentLabelAlignmentVertical: Alignment.Vertical,
    val verticalAlignmentLabelAlignmentHorizontal: Alignment.Vertical,
    val horizontalAlignmentLabelAlignmentNone: Alignment.Horizontal,
    val horizontalAlignmentLabelAlignmentVertical: Alignment.Horizontal,
    val horizontalAlignmentLabelAlignmentHorizontal: Alignment.Horizontal,
    val verticalArrangementLabelAlignmentNone: Arrangement.Vertical,
    val verticalArrangementLabelAlignmentVertical: Arrangement.Vertical,
    val horizontalArrangementLabelAlignmentHorizontal: Arrangement.Horizontal,
    val loadingSpinnerContainerWidthSizeLarge: Dp,
    val loadingSpinnerContainerWidthSizeSmall: Dp,
    val loadingSpinnerContainerWidthSizeXSmall: Dp,
    val loadingSpinnerContainerHeightSizeLarge: Dp,
    val loadingSpinnerContainerHeightSizeSmall: Dp,
    val loadingSpinnerContainerHeightSizeXSmall: Dp,
    val loadingSpinnerContainerVerticalAlignment: Alignment.Vertical,
    val loadingSpinnerContainerHorizontalAlignment: Alignment.Horizontal,
    val loadingSpinnerContainerHorizontalArrangement: Arrangement.Horizontal,
    val labelStyleLabelAlignmentHorizontal: ODSTextStyle,
    val labelStyleLabelAlignmentVertical: ODSTextStyle,
    val labelTextAlignLabelAlignmentHorizontal: TextAlign,
    val labelTextAlignLabelAlignmentVertical: TextAlign,
    var progressIndicatorStrokeWidthLarge: Dp, // Not exported from the plugin
    var progressIndicatorStrokeWidthSmall: Dp, // Not exported from the plugin
    var progressIndicatorStrokeWidthXSmall: Dp // Not exported from the plugin
)

val defaultODSLoadingSpinnerTokens = ODSLoadingSpinnerTokens(
    gapLabelAlignmentNone = DSVariables.spacingComponent4,
    gapLabelAlignmentVertical = DSVariables.spacingComponent4,
    gapLabelAlignmentHorizontal = DSVariables.spacingComponent5,
    verticalAlignmentLabelAlignmentNone = Alignment.Top,
    verticalAlignmentLabelAlignmentVertical = Alignment.Top,
    verticalAlignmentLabelAlignmentHorizontal = Alignment.CenterVertically,
    horizontalAlignmentLabelAlignmentNone = Alignment.CenterHorizontally,
    horizontalAlignmentLabelAlignmentVertical = Alignment.CenterHorizontally,
    horizontalAlignmentLabelAlignmentHorizontal = Alignment.Start,
    verticalArrangementLabelAlignmentNone = Arrangement.Top,
    verticalArrangementLabelAlignmentVertical = Arrangement.Top,
    horizontalArrangementLabelAlignmentHorizontal = Arrangement.Start,
    loadingSpinnerContainerWidthSizeLarge = DSVariables.sizingComponent14,
    loadingSpinnerContainerWidthSizeSmall = DSVariables.sizingComponent10,
    loadingSpinnerContainerWidthSizeXSmall = DSVariables.sizingComponent7,
    loadingSpinnerContainerHeightSizeLarge = DSVariables.sizingComponent14,
    loadingSpinnerContainerHeightSizeSmall = DSVariables.sizingComponent10,
    loadingSpinnerContainerHeightSizeXSmall = DSVariables.sizingComponent7,
    loadingSpinnerContainerVerticalAlignment = Alignment.Top,
    loadingSpinnerContainerHorizontalAlignment = Alignment.Start,
    loadingSpinnerContainerHorizontalArrangement = Arrangement.Start,
    labelStyleLabelAlignmentHorizontal = DSTextStyles.bodyMBold,
    labelStyleLabelAlignmentVertical = DSTextStyles.bodyMBold,
    labelTextAlignLabelAlignmentHorizontal = TextAlign.Left,
    labelTextAlignLabelAlignmentVertical = TextAlign.Left,
    progressIndicatorStrokeWidthLarge = 3.dp, // Not exported from the plugin
    progressIndicatorStrokeWidthSmall = 1.5.dp, // Not exported from the plugin
    progressIndicatorStrokeWidthXSmall = 1.dp // Not exported from the plugin

)

var DSLoadingSpinnerTokens: ODSLoadingSpinnerTokens = defaultODSLoadingSpinnerTokens
