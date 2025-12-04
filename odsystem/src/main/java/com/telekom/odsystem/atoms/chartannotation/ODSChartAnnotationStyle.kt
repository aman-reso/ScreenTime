package com.telekom.odsystem.atoms.chartannotation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSChartAnnotationStyle {
    var zStackContentAlignment: Alignment? = null // Not used in mobile
    var gap: Dp? = null // Not used in mobile
    var background: List<ODSColorModel>? = null
    var padding: ODSPadding? = null // Not used in mobile
    var cornerRadius: ODSCorners? = null
    var verticalAlignment: Alignment.Vertical? = null // Not used in mobile
    var horizontalAlignment: Alignment.Horizontal? = null // Not used in mobile
    var verticalArrangement: Arrangement.Vertical? = null // Not used in mobile
    var contentAlignment: Alignment? = null // Not used in mobile
    var tooltipBackground: Color? = null // Not exported by plugin
    fun getStyle(
        scheme: ODSTheme,
//        props: ODSChartAnnotationProps
    ): ODSChartAnnotationStyle {
        val style = ODSChartAnnotationStyle()
        /*if (props.placement == ODSChartAnnotationPlacement.TOP) {
            style.zStackContentAlignment =
                DSChartAnnotationTokens.zStackContentAlignmentPlacementTop
        }*/
        /*if (props.placement == ODSChartAnnotationPlacement.LEFT) {
            style.zStackContentAlignment =
                DSChartAnnotationTokens.zStackContentAlignmentPlacementLeft
        }
        if (props.placement == ODSChartAnnotationPlacement.RIGHT) {
            style.zStackContentAlignment =
                DSChartAnnotationTokens.zStackContentAlignmentPlacementRight
        }
        if (props.placement == ODSChartAnnotationPlacement.BOTTOM && props.alignment == ODSChartAnnotationAlignment.CENTER) {
            style.zStackContentAlignment =
                DSChartAnnotationTokens.zStackContentAlignmentPlacementBottomAlignmentCenter
        }
        if (props.placement == ODSChartAnnotationPlacement.BOTTOM && props.alignment == ODSChartAnnotationAlignment.START) {
            style.zStackContentAlignment =
                DSChartAnnotationTokens.zStackContentAlignmentPlacementBottomAlignmentStart
        }
        if (props.placement == ODSChartAnnotationPlacement.BOTTOM && props.alignment == ODSChartAnnotationAlignment.END) {
            style.zStackContentAlignment =
                DSChartAnnotationTokens.zStackContentAlignmentPlacementBottomAlignmentEnd
        }*/
        style.gap = DSChartAnnotationTokens.gap
        /*style.background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))*/
        style.padding = DSChartAnnotationTokens.padding
        style.cornerRadius = DSChartAnnotationTokens.cornerRadius
        /*if (props.placement == ODSChartAnnotationPlacement.TOP) {
            style.verticalAlignment = DSChartAnnotationTokens.verticalAlignmentPlacementTop
            style.horizontalAlignment = DSChartAnnotationTokens.horizontalAlignmentPlacementTop
            style.verticalArrangement = DSChartAnnotationTokens.verticalArrangementPlacementTop
            style.contentAlignment = DSChartAnnotationTokens.contentAlignmentPlacementTop
        }
        if (props.placement == ODSChartAnnotationPlacement.LEFT) {
            style.verticalAlignment = DSChartAnnotationTokens.verticalAlignmentPlacementLeft
            style.horizontalAlignment = DSChartAnnotationTokens.horizontalAlignmentPlacementLeft
            style.verticalArrangement = DSChartAnnotationTokens.verticalArrangementPlacementLeft
            style.contentAlignment = DSChartAnnotationTokens.contentAlignmentPlacementLeft
        }
        if (props.placement == ODSChartAnnotationPlacement.RIGHT) {
            style.verticalAlignment = DSChartAnnotationTokens.verticalAlignmentPlacementRight
            style.horizontalAlignment = DSChartAnnotationTokens.horizontalAlignmentPlacementRight
            style.verticalArrangement = DSChartAnnotationTokens.verticalArrangementPlacementRight
            style.contentAlignment = DSChartAnnotationTokens.contentAlignmentPlacementRight
        }
        if (props.placement == ODSChartAnnotationPlacement.BOTTOM && props.alignment == ODSChartAnnotationAlignment.CENTER) {
            style.verticalAlignment =
                DSChartAnnotationTokens.verticalAlignmentPlacementBottomAlignmentCenter
            style.horizontalAlignment =
                DSChartAnnotationTokens.horizontalAlignmentPlacementBottomAlignmentCenter
            style.verticalArrangement =
                DSChartAnnotationTokens.verticalArrangementPlacementBottomAlignmentCenter
            style.contentAlignment =
                DSChartAnnotationTokens.contentAlignmentPlacementBottomAlignmentCenter
        }
        if (props.placement == ODSChartAnnotationPlacement.BOTTOM && props.alignment == ODSChartAnnotationAlignment.START) {
            style.verticalAlignment =
                DSChartAnnotationTokens.verticalAlignmentPlacementBottomAlignmentStart
            style.horizontalAlignment =
                DSChartAnnotationTokens.horizontalAlignmentPlacementBottomAlignmentStart
            style.verticalArrangement =
                DSChartAnnotationTokens.verticalArrangementPlacementBottomAlignmentStart
            style.contentAlignment =
                DSChartAnnotationTokens.contentAlignmentPlacementBottomAlignmentStart
        }
        if (props.placement == ODSChartAnnotationPlacement.BOTTOM && props.alignment == ODSChartAnnotationAlignment.END) {
            style.verticalAlignment =
                DSChartAnnotationTokens.verticalAlignmentPlacementBottomAlignmentEnd
            style.horizontalAlignment =
                DSChartAnnotationTokens.horizontalAlignmentPlacementBottomAlignmentEnd
            style.verticalArrangement =
                DSChartAnnotationTokens.verticalArrangementPlacementBottomAlignmentEnd
            style.contentAlignment =
                DSChartAnnotationTokens.contentAlignmentPlacementBottomAlignmentEnd
        }*/
        style.tooltipBackground = scheme.shadesNeutralShades700.getColor()
        return style
    }
}
