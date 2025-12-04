package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.SCALE_FACTOR

data class ODSSwitchIconTokens(
    val widthSizeLarge: Dp,
    val widthSizeSmall: Dp,
    val heightSizeLarge: Dp,
    val heightSizeSmall: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalAlignmentSelected: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val contentAlignment: Alignment,
    val contentAlignmentSelected: Alignment,
    val strokeBorderRadius: ODSCorners,
    val strokeBorder: Dp,
    val strokeHeightSizeLarge: Dp,
    val strokeHeightSizeLargeStatePressed: Dp,
    val strokeHeightSizeLargeStateHovered: Dp,
    val strokeHeightSizeSmall: Dp,
    val strokeHeightSizeSmallStatePressed: Dp,
    val strokeHeightSizeSmallStateHovered: Dp,
    val strokeClipContent: Boolean,
    val strokeContentAlignment: Alignment,
    val handleContainerPadding: ODSPadding,
    val handleContainerVerticalAlignment: Alignment.Vertical,
    val handleContainerHorizontalAlignment: Alignment.Horizontal,
    val handleContainerHorizontalArrangement: Arrangement.Horizontal,
    val handleBorderRadius: ODSCorners,
    val handleWidthSizeLarge: Dp,
    val handleWidthSizeSmall: Dp,
    val handleHeightSizeLarge: Dp,
    val handleHeightSizeSmall: Dp,
    val handleClipContent: Boolean,
    val scaleFactor: Float, // Not exported by plugin
)

val defaultODSSwitchIconTokens = ODSSwitchIconTokens(
    widthSizeLarge = 76.dp,
    widthSizeSmall = 44.dp,
    heightSizeLarge = 40.dp,
    heightSizeSmall = DSVariables.sizingComponent10,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.Start,
    horizontalAlignmentSelected = Alignment.End,
    verticalArrangement = Arrangement.Center,
    contentAlignment = Alignment.CenterStart,
    contentAlignmentSelected = Alignment.CenterEnd,
    strokeBorderRadius = ODSCorners(all = DSVariables.radiusFull),
    strokeBorder = DSVariables.strokes1,
    strokeHeightSizeLarge = 40.dp,
    strokeHeightSizeLargeStatePressed = 40.dp,
    strokeHeightSizeLargeStateHovered = 44.dp,
    strokeHeightSizeSmall = 24.dp,
    strokeHeightSizeSmallStatePressed = 24.dp,
    strokeHeightSizeSmallStateHovered = 28.dp,
    strokeClipContent = true,
    strokeContentAlignment = Alignment.CenterStart,
    handleContainerPadding = ODSPadding(all = DSVariables.spacingComponent2),
    handleContainerVerticalAlignment = Alignment.CenterVertically,
    handleContainerHorizontalAlignment = Alignment.CenterHorizontally,
    handleContainerHorizontalArrangement = Arrangement.Center,
    handleBorderRadius = ODSCorners(all = DSVariables.radiusFull),
    handleWidthSizeLarge = DSVariables.sizingComponent12,
    handleWidthSizeSmall = DSVariables.sizingComponent7,
    handleHeightSizeLarge = DSVariables.sizingComponent12,
    handleHeightSizeSmall = DSVariables.sizingComponent7,
    handleClipContent = true,
    scaleFactor = SCALE_FACTOR
)

var DSSwitchIconTokens: ODSSwitchIconTokens = defaultODSSwitchIconTokens
