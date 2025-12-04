package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners

data class ODSSliderThumbTokens(
    var gap: Dp,
    var width: Dp,
    var height: Dp,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var verticalArrangement: Arrangement.Vertical,
    var thumbBorderRadius: ODSCorners,
    var thumbBorder: Dp,
    var thumbBorderStatePressed: Dp,
    var thumbBorderStateHovered: Dp,
    var thumbWidth: Dp,
    var thumbHeight: Dp,
    var thumbClipContent: Boolean,
    var thumbVerticalAlignment: Alignment.Vertical,
    var thumbHorizontalAlignment: Alignment.Horizontal,
    var thumbVerticalArrangement: Arrangement.Vertical
)

var defaultODSSliderThumbTokens = ODSSliderThumbTokens(
    gap = DSVariables.spacingComponent2,
    width = DSVariables.sizingComponent14,
    height = DSVariables.sizingMinimumTappableArea,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
    thumbBorderRadius = ODSCorners(all = DSVariables.radiusFull),
    thumbBorder = DSVariables.strokes3,
    thumbBorderStatePressed = DSVariables.strokes3,
    thumbBorderStateHovered = DSVariables.strokes2,
    thumbWidth = DSVariables.sizingComponent12,
    thumbHeight = DSVariables.sizingComponent12,
    thumbClipContent = true,
    thumbVerticalAlignment = Alignment.CenterVertically,
    thumbHorizontalAlignment = Alignment.CenterHorizontally,
    thumbVerticalArrangement = Arrangement.Center
)

var DSSliderThumbTokens: ODSSliderThumbTokens = defaultODSSliderThumbTokens
