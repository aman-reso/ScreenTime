package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSOffset

data class ODSTabsTokens(
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val contentAlignment: Alignment,
    val dividerFrameBorderRadius: ODSCorners,
    val dividerFrameHeight: Dp,
    val dividerFrameOffset: ODSOffset,
    val dividerFrameVerticalAlignment: Alignment.Vertical,
    val dividerFrameHorizontalAlignment: Alignment.Horizontal,
    val dividerFrameHorizontalArrangement: Arrangement.Horizontal,
    val dividerFrameContentAlignment: Alignment,
    val listContainerGapSizeLarge: Dp,
    val listContainerGapSizeSmall: Dp,
    val listContainerVerticalAlignment: Alignment.Vertical,
    val listContainerHorizontalAlignment: Alignment.Horizontal,
    val listContainerHorizontalArrangement: Arrangement.Horizontal,
    val lineContainerBorderRadius: ODSCorners, // Not exported from plugin
    val lineContainerHeight: Dp, // Not exported from plugin
    val lineContainerVerticalAlignment: Alignment.Vertical, // Not exported from plugin
    val lineContainerHorizontalAlignment: Alignment.Horizontal, // Not exported from plugin
    val lineContainerHorizontalArrangement: Arrangement.Horizontal // Not exported from plugin
)

val defaultODSTabsTokens = ODSTabsTokens(
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Center,
    contentAlignment = Alignment.CenterStart,
    dividerFrameBorderRadius = ODSCorners(all = DSVariables.radiusFull),
    dividerFrameHeight = 4.dp,
    dividerFrameOffset = ODSOffset(y = 0.dp),
    dividerFrameVerticalAlignment = Alignment.CenterVertically,
    dividerFrameHorizontalAlignment = Alignment.CenterHorizontally,
    dividerFrameHorizontalArrangement = Arrangement.Center,
    dividerFrameContentAlignment = Alignment.BottomStart,
    listContainerGapSizeLarge = DSVariables.spacingComponent8,
    listContainerGapSizeSmall = DSVariables.spacingComponent6,
    listContainerVerticalAlignment = Alignment.Bottom,
    listContainerHorizontalAlignment = Alignment.Start,
    listContainerHorizontalArrangement = Arrangement.Start,
    lineContainerBorderRadius = ODSCorners(all = DSVariables.radiusFull),
    lineContainerHeight = 4.dp,
    lineContainerVerticalAlignment = Alignment.CenterVertically,
    lineContainerHorizontalAlignment = Alignment.CenterHorizontally,
    lineContainerHorizontalArrangement = Arrangement.Center
)

var DSTabsTokens: ODSTabsTokens = defaultODSTabsTokens
