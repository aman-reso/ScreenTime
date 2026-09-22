package com.telekom.odsystem.atoms.chartannotation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding

data class ODSChartAnnotationTokens(
    val zStackContentAlignmentPlacementBottomAlignmentCenter: Alignment,
    val zStackContentAlignmentPlacementTop: Alignment,
    val zStackContentAlignmentPlacementRight: Alignment,
    val zStackContentAlignmentPlacementLeft: Alignment,
    val zStackContentAlignmentPlacementBottomAlignmentStart: Alignment,
    val zStackContentAlignmentPlacementBottomAlignmentEnd: Alignment,
    val gap: Dp,
    val padding: ODSPadding,
    val cornerRadius: ODSCorners,
    val verticalAlignmentPlacementBottomAlignmentCenter: Alignment.Vertical,
    val verticalAlignmentPlacementTop: Alignment.Vertical,
    val verticalAlignmentPlacementRight: Alignment.Vertical,
    val verticalAlignmentPlacementLeft: Alignment.Vertical,
    val verticalAlignmentPlacementBottomAlignmentStart: Alignment.Vertical,
    val verticalAlignmentPlacementBottomAlignmentEnd: Alignment.Vertical,
    val horizontalAlignmentPlacementBottomAlignmentCenter: Alignment.Horizontal,
    val horizontalAlignmentPlacementTop: Alignment.Horizontal,
    val horizontalAlignmentPlacementRight: Alignment.Horizontal,
    val horizontalAlignmentPlacementLeft: Alignment.Horizontal,
    val horizontalAlignmentPlacementBottomAlignmentStart: Alignment.Horizontal,
    val horizontalAlignmentPlacementBottomAlignmentEnd: Alignment.Horizontal,
    val verticalArrangementPlacementBottomAlignmentCenter: Arrangement.Vertical,
    val verticalArrangementPlacementTop: Arrangement.Vertical,
    val verticalArrangementPlacementRight: Arrangement.Vertical,
    val verticalArrangementPlacementLeft: Arrangement.Vertical,
    val verticalArrangementPlacementBottomAlignmentStart: Arrangement.Vertical,
    val verticalArrangementPlacementBottomAlignmentEnd: Arrangement.Vertical,
    val contentAlignmentPlacementBottomAlignmentCenter: Alignment,
    val contentAlignmentPlacementTop: Alignment,
    val contentAlignmentPlacementRight: Alignment,
    val contentAlignmentPlacementLeft: Alignment,
    val contentAlignmentPlacementBottomAlignmentStart: Alignment,
    val contentAlignmentPlacementBottomAlignmentEnd: Alignment
)

val defaultODSChartAnnotationTokens = ODSChartAnnotationTokens(
    zStackContentAlignmentPlacementBottomAlignmentCenter = Alignment.Center,
    zStackContentAlignmentPlacementTop = Alignment.TopStart,
    zStackContentAlignmentPlacementRight = Alignment.TopStart,
    zStackContentAlignmentPlacementLeft = Alignment.TopStart,
    zStackContentAlignmentPlacementBottomAlignmentStart = Alignment.TopStart,
    zStackContentAlignmentPlacementBottomAlignmentEnd = Alignment.TopStart,
    gap = DSVariables.spacingComponent2,
    padding = ODSPadding(
        top = DSVariables.spacingComponent3,
        bottom = DSVariables.spacingComponent3,
        left = DSVariables.spacingComponent4,
        right = DSVariables.spacingComponent4
    ),
    cornerRadius = ODSCorners(all = 4.dp),
    verticalAlignmentPlacementBottomAlignmentCenter = Alignment.CenterVertically,
    verticalAlignmentPlacementTop = Alignment.Top,
    verticalAlignmentPlacementRight = Alignment.Top,
    verticalAlignmentPlacementLeft = Alignment.Top,
    verticalAlignmentPlacementBottomAlignmentStart = Alignment.Top,
    verticalAlignmentPlacementBottomAlignmentEnd = Alignment.Top,
    horizontalAlignmentPlacementBottomAlignmentCenter = Alignment.CenterHorizontally,
    horizontalAlignmentPlacementTop = Alignment.Start,
    horizontalAlignmentPlacementRight = Alignment.Start,
    horizontalAlignmentPlacementLeft = Alignment.Start,
    horizontalAlignmentPlacementBottomAlignmentStart = Alignment.Start,
    horizontalAlignmentPlacementBottomAlignmentEnd = Alignment.Start,
    verticalArrangementPlacementBottomAlignmentCenter = Arrangement.Center,
    verticalArrangementPlacementTop = Arrangement.Top,
    verticalArrangementPlacementRight = Arrangement.Top,
    verticalArrangementPlacementLeft = Arrangement.Top,
    verticalArrangementPlacementBottomAlignmentStart = Arrangement.Top,
    verticalArrangementPlacementBottomAlignmentEnd = Arrangement.Top,
    contentAlignmentPlacementBottomAlignmentCenter = Alignment.Center,
    contentAlignmentPlacementTop = Alignment.TopStart,
    contentAlignmentPlacementRight = Alignment.TopStart,
    contentAlignmentPlacementLeft = Alignment.TopStart,
    contentAlignmentPlacementBottomAlignmentStart = Alignment.TopStart,
    contentAlignmentPlacementBottomAlignmentEnd = Alignment.TopStart
)

var DSChartAnnotationTokens: ODSChartAnnotationTokens = defaultODSChartAnnotationTokens
