package com.telekom.odsystem.atoms.tooltip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSTooltipTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSTooltipStyle {
    var gap: Dp? = null
    var backgroundColor: List<ODSColorModel>? = null
    var padding: ODSPadding? = null
    var borderRadius: ODSCorners? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var contentAlignment: Alignment? = null
    var labelTextStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    var labelMaxWidth: Dp? = null
    var containerVerticalAlignment: Alignment.Vertical? = null // Not exported from the plugin
    var containerHorizontalAlignment: Alignment.Horizontal? = null // Not exported from the plugin
    var containerVerticalArrangement: Arrangement.Vertical? = null // Not exported from the plugin
    var containerHorizontalArrangement: Arrangement.Horizontal? = null // Not exported from the plugin
    var caretHeight: Dp? = null // Not exported from the plugin
    var caretWidth: Dp? = null // Not exported from the plugin
    var caretPadding: ODSPadding? = null // Not exported from the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSTooltipProps
    ): ODSTooltipStyle {
        val style = ODSTooltipStyle()
        style.gap = DSTooltipTokens.gap
        style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
        style.padding = DSTooltipTokens.padding
        style.borderRadius = DSTooltipTokens.borderRadius
        style.verticalAlignment = DSTooltipTokens.verticalAlignment
        style.horizontalAlignment = DSTooltipTokens.horizontalAlignment
        style.verticalArrangement = DSTooltipTokens.verticalArrangement
        style.contentAlignment = DSTooltipTokens.contentAlignment
        style.labelTextStyle = DSTooltipTokens.labelTextStyle
        style.labelColor = scheme.basicTextOnAccentSecondary
        style.labelTextAlign = DSTooltipTokens.labelTextAlign
        style.labelMaxWidth = DSTooltipTokens.labelMaxWidth
        // Not exported from the plugin
        if (props.placement == ODSTooltipPlacement.TOP) {
            style.containerVerticalAlignment = DSTooltipTokens.containerVerticalAlignmentTop
            style.containerVerticalArrangement = DSTooltipTokens.containerVerticalArrangementTop
            style.caretWidth = DSTooltipTokens.caretVerticalWidth
            style.caretHeight = DSTooltipTokens.caretVerticalHeight
        }
        if (props.placement == ODSTooltipPlacement.BOTTOM) {
            style.containerVerticalAlignment = DSTooltipTokens.containerVerticalAlignmentBottom
            style.containerVerticalArrangement = DSTooltipTokens.containerVerticalArrangementBottom
            style.caretWidth = DSTooltipTokens.caretVerticalWidth
            style.caretHeight = DSTooltipTokens.caretVerticalHeight
        }
        if (props.placement == ODSTooltipPlacement.LEFT) {
            style.containerHorizontalAlignment = DSTooltipTokens.containerHorizontalAlignmentStart
            style.containerHorizontalArrangement =
                DSTooltipTokens.containerHorizontalArrangementStart
            style.containerVerticalAlignment = Alignment.CenterVertically
            style.caretWidth = DSTooltipTokens.caretHorizontalWidth
            style.caretHeight = DSTooltipTokens.caretHorizontalHeight
        }
        if (props.placement == ODSTooltipPlacement.RIGHT) {
            style.containerHorizontalAlignment = DSTooltipTokens.containerHorizontalAlignmentEnd
            style.containerHorizontalArrangement = DSTooltipTokens.containerHorizontalArrangementEnd
            style.containerVerticalAlignment = Alignment.CenterVertically
            style.caretWidth = DSTooltipTokens.caretHorizontalWidth
            style.caretHeight = DSTooltipTokens.caretHorizontalHeight
        }
        if (props.placement == ODSTooltipPlacement.TOP || props.placement == ODSTooltipPlacement.BOTTOM) {
            if (props.alignment == ODSTooltipAlignment.CENTER) {
                style.containerHorizontalAlignment =
                    DSTooltipTokens.containerHorizontalAlignmentCenter
                style.containerHorizontalArrangement =
                    DSTooltipTokens.containerHorizontalArrangementCenter
            }
            if (props.alignment == ODSTooltipAlignment.START) {
                style.containerHorizontalAlignment =
                    DSTooltipTokens.containerHorizontalAlignmentStart
                style.containerHorizontalArrangement =
                    DSTooltipTokens.containerHorizontalArrangementStart
                style.caretPadding = DSTooltipTokens.caretPaddingLeftAlignment
            }
            if (props.alignment == ODSTooltipAlignment.END) {
                style.containerHorizontalAlignment = DSTooltipTokens.containerHorizontalAlignmentEnd
                style.containerHorizontalArrangement =
                    DSTooltipTokens.containerHorizontalArrangementEnd
                style.caretPadding = DSTooltipTokens.caretPaddingRightAlignment
            }
        }
        if (props.placement == ODSTooltipPlacement.LEFT || props.placement == ODSTooltipPlacement.RIGHT) {
            if (props.alignment == ODSTooltipAlignment.CENTER) {
                style.containerVerticalAlignment = Alignment.CenterVertically
            }
            if (props.alignment == ODSTooltipAlignment.START) {
                style.containerVerticalAlignment = Alignment.Top
                style.caretPadding = DSTooltipTokens.caretPaddingTopAlignment
            }
            if (props.alignment == ODSTooltipAlignment.END) {
                style.containerVerticalAlignment = Alignment.Bottom
                style.caretPadding = DSTooltipTokens.caretPaddingBottomAlignment
            }
        }
        return style
    }
}
