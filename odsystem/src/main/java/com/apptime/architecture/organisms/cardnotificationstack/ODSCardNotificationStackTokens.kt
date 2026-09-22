package com.telekom.odsystem.organisms.cardnotificationstack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding

data class ODSCardNotificationStackTokens(
    val minWidth: Dp,
    val width: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val cardContainersVerticalAlignment: Alignment.Vertical,
    val cardContainersHorizontalAlignment: Alignment.Horizontal,
    val cardContainersVerticalArrangement: Arrangement.Vertical,
    val cardHolder1Padding: ODSPadding,
    val cardHolder1VerticalAlignment: Alignment.Vertical,
    val cardHolder1HorizontalAlignment: Alignment.Horizontal,
    val cardHolder1VerticalArrangement: Arrangement.Vertical,
    val cardBgBorderRadius: ODSCorners,
    val cardBgHeight: Dp,
    val cardBgClipContent: Boolean,
    val cardBgVerticalAlignment: Alignment.Vertical,
    val cardBgHorizontalAlignment: Alignment.Horizontal,
    val cardBgVerticalArrangement: Arrangement.Vertical,
    val cardHolder2Padding: ODSPadding,
    val cardHolder2VerticalAlignment: Alignment.Vertical,
    val cardHolder2HorizontalAlignment: Alignment.Horizontal,
    val cardHolder2VerticalArrangement: Arrangement.Vertical,
    val cardBg2BorderRadius: ODSCorners,
    val cardBg2Height: Dp,
    val cardBg2ClipContent: Boolean,
    val cardBg2VerticalAlignment: Alignment.Vertical,
    val cardBg2HorizontalAlignment: Alignment.Horizontal,
    val cardBg2VerticalArrangement: Arrangement.Vertical,
    val viewAllVerticalAlignment: Alignment.Vertical,
    val viewAllHorizontalAlignmentViewAllCentered: Alignment.Horizontal,
    val viewAllHorizontalAlignmentViewAllRightSide: Alignment.Horizontal,
    val viewAllHorizontalAlignmentViewAllLeftSide: Alignment.Horizontal,
    val viewAllVerticalArrangement: Arrangement.Vertical
)

val defaultODSCardNotificationStackTokens = ODSCardNotificationStackTokens(
    minWidth = 312.dp,
    width = DSVariables.columns6Columns,
    verticalAlignment = Alignment.Bottom,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Bottom,
    cardContainersVerticalAlignment = Alignment.Top,
    cardContainersHorizontalAlignment = Alignment.Start,
    cardContainersVerticalArrangement = Arrangement.Top,
    cardHolder1Padding = ODSPadding(
        left = DSVariables.spacingComponent5,
        right = DSVariables.spacingComponent5
    ),
    cardHolder1VerticalAlignment = Alignment.Top,
    cardHolder1HorizontalAlignment = Alignment.CenterHorizontally,
    cardHolder1VerticalArrangement = Arrangement.Top,
    cardBgBorderRadius = ODSCorners(
        topLeft = 0.dp,
        topRight = 0.dp,
        bottomLeft = DSVariables.radiusMedium,
        bottomRight = DSVariables.radiusMedium
    ),
    cardBgHeight = DSVariables.sizingComponent6,
    cardBgClipContent = true,
    cardBgVerticalAlignment = Alignment.Bottom,
    cardBgHorizontalAlignment = Alignment.Start,
    cardBgVerticalArrangement = Arrangement.Bottom,
    cardHolder2Padding = ODSPadding(
        left = DSVariables.spacingComponent8,
        right = DSVariables.spacingComponent8
    ),
    cardHolder2VerticalAlignment = Alignment.Top,
    cardHolder2HorizontalAlignment = Alignment.CenterHorizontally,
    cardHolder2VerticalArrangement = Arrangement.Top,
    cardBg2BorderRadius = ODSCorners(
        topLeft = 0.dp,
        topRight = 0.dp,
        bottomLeft = DSVariables.radiusMedium,
        bottomRight = DSVariables.radiusMedium
    ),
    cardBg2Height = DSVariables.sizingComponent6,
    cardBg2ClipContent = true,
    cardBg2VerticalAlignment = Alignment.Bottom,
    cardBg2HorizontalAlignment = Alignment.Start,
    cardBg2VerticalArrangement = Arrangement.Bottom,
    viewAllVerticalAlignment = Alignment.Bottom,
    viewAllHorizontalAlignmentViewAllCentered = Alignment.CenterHorizontally,
    viewAllHorizontalAlignmentViewAllRightSide = Alignment.End,
    viewAllHorizontalAlignmentViewAllLeftSide = Alignment.Start,
    viewAllVerticalArrangement = Arrangement.Bottom
)

var DSCardNotificationStackTokens: ODSCardNotificationStackTokens =
    defaultODSCardNotificationStackTokens
