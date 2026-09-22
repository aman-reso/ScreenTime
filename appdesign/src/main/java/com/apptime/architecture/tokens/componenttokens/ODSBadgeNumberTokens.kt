package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSBadgeNumberTokens(
    var paddingSizeStandard: ODSPadding,
    var paddingSizeLarge: ODSPadding,
    var borderRadius: ODSCorners,
    var maxWidthSizeStandard: Dp,
    var maxWidthSizeSmall: Dp,
    var maxWidthSizeLarge: Dp,
    var minHeightSizeStandard: Dp,
    var minHeightSizeSmall: Dp,
    var minHeightSizeLarge: Dp,
    var minWidthSizeStandard: Dp,
    var minWidthSizeSmall: Dp,
    var minWidthSizeLarge: Dp,
    var clipContent: Boolean,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var verticalArrangement: Arrangement.Vertical,
    var borderSizeSmall: Dp,
    var maxHeightSizeSmall: Dp,
    var digitsTextStyleSizeStandard: ODSTextStyle,
    var digitsTextStyleSizeLarge: ODSTextStyle,
    var digitsTextAlignSizeStandard: TextAlign,
    var digitsTextAlignSizeLarge: TextAlign
)

var defaultODSBadgeNumberTokens = ODSBadgeNumberTokens(
    paddingSizeStandard = ODSPadding(
        left = DSVariables.spacingComponent2,
        right = DSVariables.spacingComponent2
    ),
    paddingSizeLarge = ODSPadding(
        left = DSVariables.spacingComponent2,
        right = DSVariables.spacingComponent2
    ),
    borderRadius = ODSCorners(all = DSVariables.radiusFull),
    maxWidthSizeStandard = DSVariables.sizingComponent14,
    maxWidthSizeSmall = 10.dp,
    maxWidthSizeLarge = DSVariables.sizingComponent15,
    minHeightSizeStandard = DSVariables.sizingComponent7,
    minHeightSizeSmall = 10.dp,
    minHeightSizeLarge = DSVariables.sizingComponent9,
    minWidthSizeStandard = DSVariables.sizingComponent7,
    minWidthSizeSmall = 10.dp,
    minWidthSizeLarge = DSVariables.sizingComponent9,
    clipContent = true,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    borderSizeSmall = DSVariables.strokes1,
    maxHeightSizeSmall = 10.dp,
    digitsTextStyleSizeStandard = DSTextStyles.microcopyBold,
    digitsTextStyleSizeLarge = DSTextStyles.bodyMBold,
    digitsTextAlignSizeStandard = TextAlign.Center,
    digitsTextAlignSizeLarge = TextAlign.Center
)

var DSBadgeNumberTokens: ODSBadgeNumberTokens = defaultODSBadgeNumberTokens
