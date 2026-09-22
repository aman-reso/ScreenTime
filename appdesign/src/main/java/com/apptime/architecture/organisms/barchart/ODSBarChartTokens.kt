package com.telekom.odsystem.organisms.barchart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSBarChartTokens(
    val maxWidthDirectionVertical: Dp,
    val minWidthDirectionVertical: Dp,
    val heightDirectionVertical: Dp,
    val heightDirectionVerticalSelectedShowLine: Dp,
    val heightDirectionVerticalSelected: Dp,
    val heightDirectionHorizontal: Dp,
    val verticalAlignmentDirectionVertical: Alignment.Vertical,
    val verticalAlignmentDirectionHorizontal: Alignment.Vertical,
    val horizontalAlignmentDirectionVertical: Alignment.Horizontal,
    val horizontalAlignmentDirectionHorizontal: Alignment.Horizontal,
    val verticalArrangementDirectionVertical: Arrangement.Vertical,
    val gap: Dp,
    val maxHeightDirectionHorizontal: Dp,
    val minHeightDirectionHorizontal: Dp,
    val widthDirectionHorizontal: Dp,
    val widthDirectionHorizontalSelectedShowLine: Dp,
    val widthDirectionHorizontalSelected: Dp,
    val horizontalArrangementDirectionHorizontal: Arrangement.Horizontal,
    val odsChartLineHeightDirectionVerticalShowLine: Dp,
    val fillBarCornerRadiusShapeSquared: ODSCorners,
    val fillBarCornerRadiusShapePilled: ODSCorners,
    val fillBarVerticalAlignmentDirectionVertical: Alignment.Vertical,
    val fillBarVerticalAlignmentDirectionHorizontal: Alignment.Vertical,
    val fillBarHorizontalAlignmentDirectionVertical: Alignment.Horizontal,
    val fillBarHorizontalAlignmentDirectionHorizontal: Alignment.Horizontal,
    val fillBarVerticalArrangementDirectionVertical: Arrangement.Vertical,
    val fillBarVerticalArrangementDirectionHorizontal: Arrangement.Vertical,
    var labelTextStyle: ODSTextStyle?, // Not exported from plugin
    var guideLineThickness: Dp?, // Not exported from plugin
    var tooltipLineThickness: Dp?, // Not exported from plugin
    var tooltipTextStyle: ODSTextStyle?, // Not exported from plugin
    var barSpacing: Dp?, // Not exported from plugin
)

val defaultODSBarItemTokens = ODSBarChartTokens(
    maxWidthDirectionVertical = DSVariables.sizingMinimumTappableArea,
    minWidthDirectionVertical = DSVariables.sizingComponent6,
    heightDirectionVertical = 130.dp,
    heightDirectionVerticalSelectedShowLine = 221.dp,
    heightDirectionVerticalSelected = 170.dp,
    heightDirectionHorizontal = DSVariables.sizingMinimumTappableArea,
    verticalAlignmentDirectionVertical = Alignment.Bottom,
    verticalAlignmentDirectionHorizontal = Alignment.CenterVertically,
    horizontalAlignmentDirectionVertical = Alignment.CenterHorizontally,
    horizontalAlignmentDirectionHorizontal = Alignment.Start,
    verticalArrangementDirectionVertical = Arrangement.Bottom,
    gap = 6.dp,
    maxHeightDirectionHorizontal = DSVariables.sizingMinimumTappableArea,
    minHeightDirectionHorizontal = DSVariables.sizingComponent6,
    widthDirectionHorizontal = 110.dp,
    widthDirectionHorizontalSelectedShowLine = 277.dp,
    widthDirectionHorizontalSelected = 210.dp,
    horizontalArrangementDirectionHorizontal = Arrangement.Start,
    odsChartLineHeightDirectionVerticalShowLine = 57.dp,
    fillBarCornerRadiusShapeSquared = ODSCorners(all = DSVariables.radiusExtraSmall),
    fillBarCornerRadiusShapePilled = ODSCorners(all = DSVariables.radiusFull),
    fillBarVerticalAlignmentDirectionVertical = Alignment.Bottom,
    fillBarVerticalAlignmentDirectionHorizontal = Alignment.Top,
    fillBarHorizontalAlignmentDirectionVertical = Alignment.CenterHorizontally,
    fillBarHorizontalAlignmentDirectionHorizontal = Alignment.Start,
    fillBarVerticalArrangementDirectionVertical = Arrangement.Bottom,
    fillBarVerticalArrangementDirectionHorizontal = Arrangement.Top,
    labelTextStyle = DSTextStyles.bodyMBold,
    guideLineThickness = 1.dp,
    tooltipLineThickness = 1.dp,
    tooltipTextStyle = DSTextStyles.bodySBold,
    barSpacing = 16.dp
)

var DSBarItemTokens: ODSBarChartTokens = defaultODSBarItemTokens
