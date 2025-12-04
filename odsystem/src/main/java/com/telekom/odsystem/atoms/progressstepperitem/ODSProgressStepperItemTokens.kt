package com.telekom.odsystem.atoms.progressstepperitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-29 (v1.33.1) - uid: 102eca88
 * Figma link: https://figma.com/design/cpaNsDgmzEjyHilbYDSKS9/Exploration File_Part 2?node-id=2032-21840
 */

data class ODSProgressStepperItemTokens(
    val paddingTypeCurrent: ODSPadding,
    val paddingTypeNext: ODSPadding,
    val cornerRadius: ODSCorners,
    val minHeightSizeStandard: Dp,
    val minWidthSizeStandard: Dp,
    val clipContent: Boolean,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val borderTypeNext: Dp,
    val widthSizeSmall: Dp,
    val heightSizeSmall: Dp,
    val digitsStyleTypeNextSizeStandard: ODSTextStyle,
    val digitsStyleTypeCurrentSizeStandard: ODSTextStyle,
    val digitsTextAlignTypeNextSizeStandard: TextAlign,
    val digitsTextAlignTypeCurrentSizeStandard: TextAlign,
    val checkmarkWidthTypeSuccessSizeStandard: Dp,
    val checkmarkWidthTypeSuccessSizeSmall: Dp,
    val checkmarkHeightTypeSuccessSizeStandard: Dp,
    val checkmarkHeightTypeSuccessSizeSmall: Dp,
    val highPriorityWidthTypeErrorSizeStandard: Dp,
    val highPriorityWidthTypeErrorSizeSmall: Dp,
    val highPriorityHeightTypeErrorSizeStandard: Dp,
    val highPriorityHeightTypeErrorSizeSmall: Dp,
)

val defaultODSProgressStepperItemTokens = ODSProgressStepperItemTokens(
    paddingTypeCurrent = ODSPadding(
        left = DSVariables.spacingComponent1,
        right = DSVariables.spacingComponent1
    ),
    paddingTypeNext = ODSPadding(
        left = DSVariables.spacingComponent1,
        right = DSVariables.spacingComponent1
    ),
    cornerRadius = ODSCorners(all = DSVariables.radiusFull),
    minHeightSizeStandard = DSVariables.sizingComponent7,
    minWidthSizeStandard = DSVariables.sizingComponent7,
    clipContent = true,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    borderTypeNext = DSVariables.strokes1,
    widthSizeSmall = DSVariables.sizingComponent6,
    heightSizeSmall = DSVariables.sizingComponent6,
    digitsStyleTypeNextSizeStandard = DSTextStyles.microcopyBold,
    digitsStyleTypeCurrentSizeStandard = DSTextStyles.microcopyBold,
    digitsTextAlignTypeNextSizeStandard = TextAlign.Center,
    digitsTextAlignTypeCurrentSizeStandard = TextAlign.Center,
    checkmarkWidthTypeSuccessSizeStandard = DSVariables.sizingComponent6,
    checkmarkWidthTypeSuccessSizeSmall = DSVariables.sizingComponent5,
    checkmarkHeightTypeSuccessSizeStandard = DSVariables.sizingComponent6,
    checkmarkHeightTypeSuccessSizeSmall = DSVariables.sizingComponent5,
    highPriorityWidthTypeErrorSizeStandard = DSVariables.sizingComponent6,
    highPriorityWidthTypeErrorSizeSmall = DSVariables.sizingComponent5,
    highPriorityHeightTypeErrorSizeStandard = DSVariables.sizingComponent6,
    highPriorityHeightTypeErrorSizeSmall = DSVariables.sizingComponent5
)

var DSProgressStepperItemTokens: ODSProgressStepperItemTokens = defaultODSProgressStepperItemTokens
