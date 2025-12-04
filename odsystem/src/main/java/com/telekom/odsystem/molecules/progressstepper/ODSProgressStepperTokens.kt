package com.telekom.odsystem.molecules.progressstepper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-29 (v1.33.1) - uid: 102ecae4
 * Figma link: https://figma.com/design/cpaNsDgmzEjyHilbYDSKS9/Exploration File_Part 2?node-id=2032-21811
 */

data class ODSProgressStepperTokens(
    val gapVariantVerticalSizeStandard: Dp,
    val gapVariantVerticalSizeSmall: Dp,
    val gapVariantHorizontal: Dp,
    val minHeightVariantVertical: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangementVariantVertical: Arrangement.Horizontal,
    val verticalArrangementVariantHorizontal: Arrangement.Vertical,
    val badgeDividerFrameGapVariantVertical: Dp,
    val badgeDividerFrameGapVariantHorizontalSizeSmall: Dp,
    val badgeDividerFrameGapVariantHorizontalSizeStandard: Dp,
    val badgeDividerFramePaddingVariantVerticalSizeStandard: ODSPadding,
    val badgeDividerFramePaddingVariantVerticalSizeSmall: ODSPadding,
    val badgeDividerFrameVerticalAlignmentVariantVertical: Alignment.Vertical,
    val badgeDividerFrameVerticalAlignmentVariantHorizontal: Alignment.Vertical,
    val badgeDividerFrameHorizontalAlignment: Alignment.Horizontal,
    val badgeDividerFrameVerticalArrangementVariantVertical: Arrangement.Vertical,
    val badgeDividerFrameHorizontalArrangementVariantHorizontal: Arrangement.Horizontal,
    val contentFrameGapVariantVertical: Dp,
    val contentFrameGapVariantHorizontalSizeStandard: Dp,
    val contentFramePaddingVariantVertical: ODSPadding,
    val contentFramePaddingVariantHorizontal: ODSPadding,
    val contentFrameVerticalAlignment: Alignment.Vertical,
    val contentFrameHorizontalAlignment: Alignment.Horizontal,
    val contentFrameVerticalArrangement: Arrangement.Vertical,
    val labelStyle: ODSTextStyle,
    val labelTextAlign: TextAlign,
    val textStyleSizeStandard: ODSTextStyle,
    val textStyleSizeSmall: ODSTextStyle,
    val textTextAlign: TextAlign,
)

val defaultODSProgressStepperTokens = ODSProgressStepperTokens(
    gapVariantVerticalSizeStandard = DSVariables.spacingComponent3,
    gapVariantVerticalSizeSmall = DSVariables.spacingComponent2,
    gapVariantHorizontal = DSVariables.spacingComponent1,
    minHeightVariantVertical = DSVariables.sizingComponent16,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    horizontalArrangementVariantVertical = Arrangement.Start,
    verticalArrangementVariantHorizontal = Arrangement.Top,
    badgeDividerFrameGapVariantVertical = DSVariables.spacingComponent2,
    badgeDividerFrameGapVariantHorizontalSizeSmall = DSVariables.spacingComponent2,
    badgeDividerFrameGapVariantHorizontalSizeStandard = DSVariables.spacingComponent3,
    badgeDividerFramePaddingVariantVerticalSizeStandard = ODSPadding(top = DSVariables.spacingComponent1),
    badgeDividerFramePaddingVariantVerticalSizeSmall = ODSPadding(top = DSVariables.spacingComponent2),
    badgeDividerFrameVerticalAlignmentVariantVertical = Alignment.Top,
    badgeDividerFrameVerticalAlignmentVariantHorizontal = Alignment.CenterVertically,
    badgeDividerFrameHorizontalAlignment = Alignment.CenterHorizontally,
    badgeDividerFrameVerticalArrangementVariantVertical = Arrangement.Top,
    badgeDividerFrameHorizontalArrangementVariantHorizontal = Arrangement.Center,
    contentFrameGapVariantVertical = DSVariables.spacingComponent1,
    contentFrameGapVariantHorizontalSizeStandard = DSVariables.spacingComponent1,
    contentFramePaddingVariantVertical = ODSPadding(
        bottom = DSVariables.spacingComponent5,
        right = DSVariables.spacingComponent3
    ),
    contentFramePaddingVariantHorizontal = ODSPadding(right = DSVariables.spacingComponent3),
    contentFrameVerticalAlignment = Alignment.CenterVertically,
    contentFrameHorizontalAlignment = Alignment.Start,
    contentFrameVerticalArrangement = Arrangement.Center,
    labelStyle = DSTextStyles.bodyMBold,
    labelTextAlign = TextAlign.Left,
    textStyleSizeStandard = DSTextStyles.bodyMRegular,
    textStyleSizeSmall = DSTextStyles.bodySRegular,
    textTextAlign = TextAlign.Left
)

var DSProgressStepperTokens: ODSProgressStepperTokens = defaultODSProgressStepperTokens
