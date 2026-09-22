package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding

data class ODSRadioIconTokens(
    val padding: ODSPadding,
    val paddingStatePressed: ODSPadding,
    val paddingStateHovered: ODSPadding,
    val widthSizeSmall: Dp,
    val widthSizeLarge: Dp,
    val heightSizeSmall: Dp,
    val heightSizeLarge: Dp,
    val clipContent: Boolean,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val iconBorderRadius: ODSCorners,
    val iconVerticalAlignment: Alignment.Vertical,
    val iconHorizontalAlignment: Alignment.Horizontal,
    val iconHorizontalArrangement: Arrangement.Horizontal,
    val iconBorder: Dp,
    val iconBorderError: Dp,
    val innerCircleBorderRadius: ODSCorners,
    val innerCircleWidthSizeSmall: Dp, // Custom addition to match base
    val innerCircleWidthSizeLarge: Dp, // Custom addition to match base
    val innerCircleWidthStatePressedSizeSmall: Dp, // Custom addition to match base
    val innerCircleWidthStatePressedSizeLarge: Dp, // Custom addition to match base
    val innerCircleWidthSizeLargeStateHovered: Dp,
    val innerCircleWidthSizeSmallStateHovered: Dp,
    val innerCircleHeightSizeSmall: Dp, // Custom addition to match base
    val innerCircleHeightSizeLarge: Dp, // Custom addition to match base
    val innerCircleHeightStatePressedSizeLarge: Dp, // Custom addition to match base
    val innerCircleHeightStatePressedSizeSmall: Dp, // Custom addition to match base
    val innerCircleHeightSizeLargeStateHovered: Dp,
    val innerCircleHeightSizeSmallStateHovered: Dp,
    val innerCircleClipContent: Boolean
)

val defaultODSRadioIconTokens = ODSRadioIconTokens(
    padding = ODSPadding(all = DSVariables.spacingComponent1),
    paddingStatePressed = ODSPadding(all = DSVariables.spacingComponent1),
    paddingStateHovered = ODSPadding(
        top = DSVariables.spacingComponent0,
        bottom = DSVariables.spacingComponent0,
        left = DSVariables.spacingComponent0,
        right = DSVariables.spacingComponent0
    ),
    widthSizeSmall = DSVariables.sizingComponent10,
    widthSizeLarge = DSVariables.sizingComponent12,
    heightSizeSmall = DSVariables.sizingComponent10,
    heightSizeLarge = DSVariables.sizingComponent12,
    clipContent = true,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center,
    iconBorderRadius = ODSCorners(all = DSVariables.radiusFull),
    iconVerticalAlignment = Alignment.CenterVertically,
    iconHorizontalAlignment = Alignment.CenterHorizontally,
    iconHorizontalArrangement = Arrangement.Center,
    iconBorder = DSVariables.strokes1,
    iconBorderError = DSVariables.strokes3,
    innerCircleBorderRadius = ODSCorners(all = DSVariables.radiusFull),
    innerCircleWidthSizeSmall = DSVariables.sizingComponent4,
    innerCircleWidthSizeLarge = DSVariables.sizingComponent4,
    innerCircleWidthStatePressedSizeSmall = DSVariables.sizingComponent4,
    innerCircleWidthStatePressedSizeLarge = DSVariables.sizingComponent4,
    innerCircleWidthSizeLargeStateHovered = DSVariables.sizingComponent6,
    innerCircleWidthSizeSmallStateHovered = DSVariables.sizingComponent5,
    innerCircleHeightSizeSmall = DSVariables.sizingComponent4,
    innerCircleHeightSizeLarge = DSVariables.sizingComponent4,
    innerCircleHeightStatePressedSizeSmall = DSVariables.sizingComponent4,
    innerCircleHeightStatePressedSizeLarge = DSVariables.sizingComponent4,
    innerCircleHeightSizeLargeStateHovered = DSVariables.sizingComponent6,
    innerCircleHeightSizeSmallStateHovered = DSVariables.sizingComponent5,
    innerCircleClipContent = true
)

var DSRadioIconTokens: ODSRadioIconTokens = defaultODSRadioIconTokens
