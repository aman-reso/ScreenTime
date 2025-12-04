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

data class ODSTabItemTokens(
    val minHeightSizeLarge: Dp,
    val minHeightSizeSmall: Dp,
    val minWidth: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val contentFrameGapSizeLarge: Dp,
    val contentFrameGapSizeSmall: Dp,
    val contentFramePadding: ODSPadding,
    val contentFrameVerticalAlignment: Alignment.Vertical,
    val contentFrameHorizontalAlignment: Alignment.Horizontal,
    val contentFrameHorizontalArrangement: Arrangement.Horizontal,
    val iconWidthSizeLarge: Dp,
    val iconWidthSizeSmall: Dp,
    val iconHeightSizeLarge: Dp,
    val iconHeightSizeSmall: Dp,
    val labelTextStyleSizeLarge: ODSTextStyle,
    val labelTextStyleSizeSmall: ODSTextStyle,
    val labelTextAlign: TextAlign,
    val lineContainerBorderRadius: ODSCorners,
    val lineContainerHeight: Dp,
    val lineContainerVerticalAlignment: Alignment.Vertical,
    val lineContainerHorizontalAlignment: Alignment.Horizontal,
    val lineContainerHorizontalArrangement: Arrangement.Horizontal
)

val defaultODSTabItemTokens = ODSTabItemTokens(
    minHeightSizeLarge = 54.dp,
    minHeightSizeSmall = DSVariables.sizingMinimumTappableArea,
    minWidth = DSVariables.sizingMinimumTappableArea,
    verticalAlignment = Alignment.Bottom,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Bottom,
    contentFrameGapSizeLarge = DSVariables.spacingComponent5,
    contentFrameGapSizeSmall = DSVariables.spacingComponent3,
    contentFramePadding = ODSPadding(
        top = DSVariables.spacingComponent3,
        bottom = DSVariables.spacingComponent3
    ),
    contentFrameVerticalAlignment = Alignment.CenterVertically,
    contentFrameHorizontalAlignment = Alignment.CenterHorizontally,
    contentFrameHorizontalArrangement = Arrangement.Center,
    iconWidthSizeLarge = DSVariables.sizingComponent9,
    iconWidthSizeSmall = DSVariables.sizingComponent7,
    iconHeightSizeLarge = DSVariables.sizingComponent9,
    iconHeightSizeSmall = DSVariables.sizingComponent7,
    labelTextStyleSizeLarge = DSTextStyles.subtitle,
    labelTextStyleSizeSmall = DSTextStyles.bodyMBold,
    labelTextAlign = TextAlign.Center,
    lineContainerBorderRadius = ODSCorners(all = DSVariables.radiusFull),
    lineContainerHeight = 4.dp,
    lineContainerVerticalAlignment = Alignment.CenterVertically,
    lineContainerHorizontalAlignment = Alignment.CenterHorizontally,
    lineContainerHorizontalArrangement = Arrangement.Center
)

var DSTabItemTokens: ODSTabItemTokens = defaultODSTabItemTokens
