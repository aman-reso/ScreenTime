package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners

data class ODSTimerSegmentTokens(
    val borderRadius: ODSCorners,
    val clipContent: Boolean,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val contentAlignment: Alignment,
    val progressBorderRadius: ODSCorners,
    val progressHeight: Dp,
    val progressClipContent: Boolean,
    val progressContentAlignment: Alignment,
    val indicatorBorderRadius: ODSCorners,
    val indicatorWidth: Dp,
    val indicatorHeight: Dp,
    val indicatorClipContent: Boolean
)

val defaultODSTimerSegmentTokens = ODSTimerSegmentTokens(
    borderRadius = ODSCorners(all = DSVariables.radiusFull),
    clipContent = true,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.End,
    horizontalArrangement = Arrangement.End,
    contentAlignment = Alignment.TopEnd,
    progressBorderRadius = ODSCorners(
        topLeft = DSVariables.radiusFull,
        topRight = 0.dp,
        bottomLeft = DSVariables.radiusFull,
        bottomRight = 0.dp
    ),
    progressHeight = DSVariables.sizingComponent3,
    progressClipContent = true,
    progressContentAlignment = Alignment.CenterStart,
    indicatorBorderRadius = ODSCorners(all = DSVariables.radiusFull),
    indicatorWidth = DSVariables.sizingComponent3,
    indicatorHeight = DSVariables.sizingComponent3,
    indicatorClipContent = true
)

var DSTimerSegmentTokens: ODSTimerSegmentTokens = defaultODSTimerSegmentTokens
