package com.telekom.odsystem.organisms.barchart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSBarChartStyle {
    var maxWidth: Dp? = null
    var minWidth: Dp? = null
    var height: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var gap: Dp? = null
    var maxHeight: Dp? = null
    var minHeight: Dp? = null
    var width: Dp? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var odsChartLineHeight: Dp? = null
    var fillBarBackground: List<ODSColorModel>? = null
    var fillBarCornerRadius: ODSCorners? = null
    var fillBarVerticalAlignment: Alignment.Vertical? = null
    var fillBarHorizontalAlignment: Alignment.Horizontal? = null
    var fillBarVerticalArrangement: Arrangement.Vertical? = null
    var selectedBarBackground: Color? = null // Not exported by plugin
    var unselectedBarBackground: Color? = null // Not exported by plugin
    var labelColor: Color? = null // Not exported by plugin
    var guideLineColor: Color? = null // Not exported by plugin
    var labelTextStyle: ODSTextStyle? = null // Not exported by plugin
    var guideLineThickness: Dp? = null // Not exported by plugin
    var tooltipBackground: Color? = null // Not exported by plugin
    var tooltipLineThickness: Dp? = null // Not exported by plugin
    var tooltipTextStyle: ODSTextStyle? = null // Not exported by plugin
    var barSpacing: Dp? = null // Not exported by plugin
    var barThickness: Dp = 28.dp // Not exported by plugin
    var barCornerRadius = DSVariables.radiusFull

    fun getStyle(
        scheme: ODSTheme,
        props: ODSBarChartProps
    ): ODSBarChartStyle {
        val style = ODSBarChartStyle()
        /*if (!props.showLine) {
            style.gap = DSBarItemTokens.gap
        }*/
        if (props.direction == ODSBarItemDirection.VERTICAL) {
            style.maxWidth = DSBarItemTokens.maxWidthDirectionVertical
            style.minWidth = DSBarItemTokens.minWidthDirectionVertical
            style.verticalAlignment = DSBarItemTokens.verticalAlignmentDirectionVertical
            style.horizontalAlignment = DSBarItemTokens.horizontalAlignmentDirectionVertical
            style.verticalArrangement = DSBarItemTokens.verticalArrangementDirectionVertical
        }
        if (props.direction == ODSBarItemDirection.HORIZONTAL) {
            style.height = DSBarItemTokens.heightDirectionHorizontal
            style.verticalAlignment = DSBarItemTokens.verticalAlignmentDirectionHorizontal
            style.horizontalAlignment = DSBarItemTokens.horizontalAlignmentDirectionHorizontal
            style.maxHeight = DSBarItemTokens.maxHeightDirectionHorizontal
            style.minHeight = DSBarItemTokens.minHeightDirectionHorizontal
            style.horizontalArrangement = DSBarItemTokens.horizontalArrangementDirectionHorizontal
        }
        /*if (props.direction == ODSBarItemDirection.VERTICAL && !props.selected) {
            style.height = DSBarItemTokens.heightDirectionVertical
        }
        if (props.direction == ODSBarItemDirection.HORIZONTAL && !props.selected) {
            style.width = DSBarItemTokens.widthDirectionHorizontal
        }
        if (props.direction == ODSBarItemDirection.VERTICAL && props.selected && props.showLine) {
            style.height = DSBarItemTokens.heightDirectionVerticalSelectedShowLine
        }
        if (props.direction == ODSBarItemDirection.VERTICAL && props.selected && !props.showLine) {
            style.height = DSBarItemTokens.heightDirectionVerticalSelected
        }
        if (props.direction == ODSBarItemDirection.HORIZONTAL && props.selected && props.showLine) {
            style.width = DSBarItemTokens.widthDirectionHorizontalSelectedShowLine
        }
        if (props.direction == ODSBarItemDirection.HORIZONTAL && props.selected && !props.showLine) {
            style.width = DSBarItemTokens.widthDirectionHorizontalSelected
        }
        if (props.direction == ODSBarItemDirection.VERTICAL && props.showLine) {
            style.odsChartLineHeight = DSBarItemTokens.odsChartLineHeightDirectionVerticalShowLine
        }
        if (props.selected) {
            style.fillBarBackground = listOf(ODSColorModel(hexColor = scheme.basicAccent))
        }
        if (!props.selected) {
            style.fillBarBackground = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
        }*/
        if (props.shape == ODSBarItemShape.PILLED) {
            style.fillBarCornerRadius = DSBarItemTokens.fillBarCornerRadiusShapePilled
        }
        if (props.shape == ODSBarItemShape.SQUARED) {
            style.fillBarCornerRadius = DSBarItemTokens.fillBarCornerRadiusShapeSquared
        }
        if (props.direction == ODSBarItemDirection.VERTICAL) {
            style.fillBarVerticalAlignment =
                DSBarItemTokens.fillBarVerticalAlignmentDirectionVertical
            style.fillBarHorizontalAlignment =
                DSBarItemTokens.fillBarHorizontalAlignmentDirectionVertical
            style.fillBarVerticalArrangement =
                DSBarItemTokens.fillBarVerticalArrangementDirectionVertical
        }
        if (props.direction == ODSBarItemDirection.HORIZONTAL) {
            style.fillBarVerticalAlignment =
                DSBarItemTokens.fillBarVerticalAlignmentDirectionHorizontal
            style.fillBarHorizontalAlignment =
                DSBarItemTokens.fillBarHorizontalAlignmentDirectionHorizontal
            style.fillBarVerticalArrangement =
                DSBarItemTokens.fillBarVerticalArrangementDirectionHorizontal
        }
        style.selectedBarBackground = scheme.basicAccent.getColor()
        style.unselectedBarBackground = scheme.basicAccentSecondary.getColor()
        style.labelColor = scheme.shadesNeutralShades600.getColor()
        style.guideLineColor = scheme.shadesNeutralShades300.getColor()
        style.labelTextStyle = DSBarItemTokens.labelTextStyle
        style.guideLineThickness = DSBarItemTokens.guideLineThickness
        style.tooltipBackground = scheme.shadesNeutralShades700.getColor()
        style.tooltipLineThickness = DSBarItemTokens.tooltipLineThickness
        style.tooltipTextStyle = DSBarItemTokens.tooltipTextStyle
        style.barSpacing = DSBarItemTokens.barSpacing
        return style
    }
}
