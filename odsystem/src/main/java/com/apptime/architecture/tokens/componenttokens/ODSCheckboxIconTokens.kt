package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding

/**
 * Code generated with ODS RADD Code Generator Plugin
 * 2025-06-16 (v1.31.6) - uid: 4592d91b
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=15509-8520
 */

data class ODSCheckboxIconTokens(
    val padding: ODSPadding,
    val paddingStatePressed: ODSPadding,
    val paddingStateHovered: ODSPadding,
    val widthSizeSmall: Dp,
    val widthSizeLarge: Dp,
    val heightSizeSmall: Dp,
    val heightSizeLarge: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val iconCornerRadiusSizeSmall: ODSCorners,
    val iconCornerRadiusSizeLarge: ODSCorners,
    val iconVerticalAlignment: Alignment.Vertical,
    val iconHorizontalAlignment: Alignment.Horizontal,
    val iconHorizontalArrangement: Arrangement.Horizontal,
    val iconBorderSelectedUnselected: Dp,
    val iconBorderSelectedUnselectedError: Dp,
    val minusWidth: Dp,
    val minusHeight: Dp,
    val checkmarkWidth: Dp,
    val checkmarkHeight: Dp
)

val defaultODSCheckboxIconTokens = ODSCheckboxIconTokens(
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
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center,
    iconCornerRadiusSizeSmall = ODSCorners(all = DSVariables.radiusExtraSmall),
    iconCornerRadiusSizeLarge = ODSCorners(all = DSVariables.radiusSmall),
    iconVerticalAlignment = Alignment.CenterVertically,
    iconHorizontalAlignment = Alignment.CenterHorizontally,
    iconHorizontalArrangement = Arrangement.Center,
    iconBorderSelectedUnselected = DSVariables.strokes1,
    iconBorderSelectedUnselectedError = DSVariables.strokes3,
    minusWidth = DSVariables.sizingComponent7,
    minusHeight = DSVariables.sizingComponent7,
    checkmarkWidth = DSVariables.sizingComponent7,
    checkmarkHeight = DSVariables.sizingComponent7
)

var DSCheckboxIconTokens: ODSCheckboxIconTokens = defaultODSCheckboxIconTokens
