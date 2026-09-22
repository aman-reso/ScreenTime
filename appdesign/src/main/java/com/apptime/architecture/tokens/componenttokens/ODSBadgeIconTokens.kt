package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding

data class ODSBadgeIconTokens(
    var borderRadius: ODSCorners,
    var widthSizeLarge: Dp,
    var widthSizeStandard: Dp,
    var widthSizeSmall: Dp,
    var heightSizeLarge: Dp,
    var heightSizeStandard: Dp,
    var heightSizeSmall: Dp,
    var clipContent: Boolean,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var verticalArrangement: Arrangement.Vertical,
    var paddingTypeError: ODSPadding,
    var borderSizeSmall: Dp,
    var checkmarkWidthTypeSuccessSizeLarge: Dp,
    var checkmarkWidthTypeSuccessSizeStandard: Dp,
    var checkmarkHeightTypeSuccessSizeLarge: Dp,
    var checkmarkHeightTypeSuccessSizeStandard: Dp,
    var errorWidthTypeErrorSizeLarge: Dp,
    var errorWidthTypeErrorSizeStandard: Dp,
    var errorHeightTypeErrorSizeLarge: Dp,
    var errorHeightTypeErrorSizeStandard: Dp
)

var defaultODSBadgeIconTokens = ODSBadgeIconTokens(
    borderRadius = ODSCorners(all = DSVariables.radiusFull),
    widthSizeLarge = DSVariables.sizingComponent9,
    widthSizeStandard = DSVariables.sizingComponent7,
    widthSizeSmall = DSVariables.sizingComponent5,
    heightSizeLarge = DSVariables.sizingComponent9,
    heightSizeStandard = DSVariables.sizingComponent7,
    heightSizeSmall = DSVariables.sizingComponent5,
    clipContent = true,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    paddingTypeError = ODSPadding(bottom = DSVariables.spacingComponent1),
    borderSizeSmall = DSVariables.strokes1,
    checkmarkWidthTypeSuccessSizeLarge = DSVariables.sizingComponent7,
    checkmarkWidthTypeSuccessSizeStandard = DSVariables.sizingComponent6,
    checkmarkHeightTypeSuccessSizeLarge = DSVariables.sizingComponent7,
    checkmarkHeightTypeSuccessSizeStandard = DSVariables.sizingComponent6,
    errorWidthTypeErrorSizeLarge = DSVariables.sizingComponent7,
    errorWidthTypeErrorSizeStandard = DSVariables.sizingComponent6,
    errorHeightTypeErrorSizeLarge = DSVariables.sizingComponent7,
    errorHeightTypeErrorSizeStandard = DSVariables.sizingComponent6
)

var DSBadgeIconTokens: ODSBadgeIconTokens = defaultODSBadgeIconTokens
