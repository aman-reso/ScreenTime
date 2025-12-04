package com.telekom.odsystem.atoms.segments

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

data class ODSSegmentsTokens(
    val borderRadius: ODSCorners,
    val minHeightSizeLarge: Dp,
    val minHeightSizeSmall: Dp,
    val minWidthSizeLarge: Dp,
    val minWidthSizeSmall: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val contentFrameGap: Dp,
    val contentFramePadding: ODSPadding,
    val contentFrameBorderRadius: ODSCorners,
    val contentFrameMinHeightSizeLarge: Dp,
    val contentFrameMinHeightSizeSmall: Dp,
    val contentFrameMinWidthSizeLarge: Dp,
    val contentFrameMinWidthSizeSmall: Dp,
    val contentFrameVerticalAlignment: Alignment.Vertical,
    val contentFrameHorizontalAlignment: Alignment.Horizontal,
    val contentFrameHorizontalArrangement: Arrangement.Horizontal,
    val iconWidthSizeLarge: Dp,
    val iconWidthSizeSmall: Dp,
    val iconHeightSizeLarge: Dp,
    val iconHeightSizeSmall: Dp,
    val labelTextStyleSizeLarge: ODSTextStyle,
    val labelTextStyleSizeSmall: ODSTextStyle,
    val labelTextAlign: TextAlign
)

val defaultODSSegmentsTokens = ODSSegmentsTokens(
    borderRadius = ODSCorners(all = DSVariables.radiusFull),
    minHeightSizeLarge = 56.dp,
    minHeightSizeSmall = DSVariables.sizingMinimumTappableArea,
    minWidthSizeLarge = 56.dp,
    minWidthSizeSmall = DSVariables.sizingMinimumTappableArea,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center,
    contentFrameGap = DSVariables.spacingComponent1,
    contentFramePadding = ODSPadding(
        left = DSVariables.spacingComponent3,
        right = DSVariables.spacingComponent3
    ),
    contentFrameBorderRadius = ODSCorners(all = DSVariables.radiusFull),
    contentFrameMinHeightSizeLarge = 56.dp,
    contentFrameMinHeightSizeSmall = 40.dp,
    contentFrameMinWidthSizeLarge = 56.dp,
    contentFrameMinWidthSizeSmall = 40.dp,
    contentFrameVerticalAlignment = Alignment.CenterVertically,
    contentFrameHorizontalAlignment = Alignment.CenterHorizontally,
    contentFrameHorizontalArrangement = Arrangement.Center,
    iconWidthSizeLarge = DSVariables.sizingComponent10,
    iconWidthSizeSmall = DSVariables.sizingComponent7,
    iconHeightSizeLarge = DSVariables.sizingComponent10,
    iconHeightSizeSmall = DSVariables.sizingComponent7,
    labelTextStyleSizeLarge = DSTextStyles.bodyMBold,
    labelTextStyleSizeSmall = DSTextStyles.bodySBold,
    labelTextAlign = TextAlign.Center
)

var DSSegmentsTokens: ODSSegmentsTokens = defaultODSSegmentsTokens
