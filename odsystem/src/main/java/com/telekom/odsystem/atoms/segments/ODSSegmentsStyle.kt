package com.telekom.odsystem.atoms.segments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSSegmentsStyle {
    var borderRadius: ODSCorners? = null
    var minHeight: Dp? = null
    var minWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var backgroundColor: List<ODSColorModel>? = null
    var contentFrameGap: Dp? = null
    var contentFrameBackgroundColor: List<ODSColorModel>? = null
    var contentFramePadding: ODSPadding? = null
    var contentFrameBorderRadius: ODSCorners? = null
    var contentFrameMinHeight: Dp? = null
    var contentFrameMinWidth: Dp? = null
    var contentFrameVerticalAlignment: Alignment.Vertical? = null
    var contentFrameHorizontalAlignment: Alignment.Horizontal? = null
    var contentFrameHorizontalArrangement: Arrangement.Horizontal? = null
    var iconColor: HexColor? = null
    var iconWidth: Dp? = null
    var iconHeight: Dp? = null
    var labelTextStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSSegmentsProps,
        state: ODSActions
    ): ODSSegmentsStyle {
        val style = ODSSegmentsStyle()
        style.borderRadius = DSSegmentsTokens.borderRadius
        style.verticalAlignment = DSSegmentsTokens.verticalAlignment
        style.horizontalAlignment = DSSegmentsTokens.horizontalAlignment
        style.horizontalArrangement = DSSegmentsTokens.horizontalArrangement
        if (props.size == ODSSegmentsSize.LARGE) {
            style.minHeight = DSSegmentsTokens.minHeightSizeLarge
            style.minWidth = DSSegmentsTokens.minWidthSizeLarge
        }
        if (props.size == ODSSegmentsSize.SMALL) {
            style.minHeight = DSSegmentsTokens.minHeightSizeSmall
            style.minWidth = DSSegmentsTokens.minWidthSizeSmall
        }
        if (props.size == ODSSegmentsSize.LARGE && props.variant == ODSSegmentsVariant.FILL && props.selected && props.disabled) {
            style.backgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
        }
        style.contentFrameGap = DSSegmentsTokens.contentFrameGap
        style.contentFramePadding = DSSegmentsTokens.contentFramePadding
        style.contentFrameBorderRadius = DSSegmentsTokens.contentFrameBorderRadius
        style.contentFrameVerticalAlignment = DSSegmentsTokens.contentFrameVerticalAlignment
        style.contentFrameHorizontalAlignment = DSSegmentsTokens.contentFrameHorizontalAlignment
        style.contentFrameHorizontalArrangement = DSSegmentsTokens.contentFrameHorizontalArrangement
        if (props.size == ODSSegmentsSize.LARGE) {
            style.contentFrameMinHeight = DSSegmentsTokens.contentFrameMinHeightSizeLarge
            style.contentFrameMinWidth = DSSegmentsTokens.contentFrameMinWidthSizeLarge
        }
        if (props.size == ODSSegmentsSize.SMALL) {
            style.contentFrameMinHeight = DSSegmentsTokens.contentFrameMinHeightSizeSmall
            style.contentFrameMinWidth = DSSegmentsTokens.contentFrameMinWidthSizeSmall
        }
        if (!props.selected && !props.disabled) {
            style.contentFrameBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
        }
        if (props.size == ODSSegmentsSize.SMALL && props.disabled) {
            style.contentFrameBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
        }
        if (props.selected && !props.disabled) {
            style.contentFrameBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicAccent))
        }
        if (!props.selected && state == ODSActions.HOVERED && !props.disabled) {
            style.contentFrameBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundSubtleHover))
        }
        if (!props.selected && state == ODSActions.PRESSED && !props.disabled) {
            style.contentFrameBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundSubtlePressed))
        }
        if (props.size == ODSSegmentsSize.LARGE && props.variant == ODSSegmentsVariant.HUG && props.disabled) {
            style.contentFrameBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
        }
        if (props.selected && state == ODSActions.HOVERED && !props.disabled) {
            style.contentFrameBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverAccentHover))
        }
        if (props.selected && state == ODSActions.PRESSED && !props.disabled) {
            style.contentFrameBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedAccentPressed))
        }
        if (props.size == ODSSegmentsSize.LARGE && props.variant == ODSSegmentsVariant.FILL && !props.selected && props.disabled) {
            style.contentFrameBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
        }
        if (props.size == ODSSegmentsSize.LARGE) {
            style.iconWidth = DSSegmentsTokens.iconWidthSizeLarge
            style.iconHeight = DSSegmentsTokens.iconHeightSizeLarge
        }
        if (props.size == ODSSegmentsSize.SMALL) {
            style.iconWidth = DSSegmentsTokens.iconWidthSizeSmall
            style.iconHeight = DSSegmentsTokens.iconHeightSizeSmall
        }
        if (!props.selected && !props.disabled) {
            style.iconColor = scheme.basicText
        }
        if (!props.selected && props.disabled) {
            style.iconColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.selected && !props.disabled) {
            style.iconColor = scheme.basicTextOnAccent
        }
        if (props.selected && props.disabled) {
            style.iconColor = scheme.interactionStatesDisabledTextOnAccentDisabled
        }
        style.labelTextAlign = DSSegmentsTokens.labelTextAlign
        if (props.size == ODSSegmentsSize.LARGE) {
            style.labelTextStyle = DSSegmentsTokens.labelTextStyleSizeLarge
        }
        if (props.size == ODSSegmentsSize.SMALL) {
            style.labelTextStyle = DSSegmentsTokens.labelTextStyleSizeSmall
        }
        if (!props.selected && !props.disabled) {
            style.labelColor = scheme.basicText
        }
        if (!props.selected && props.disabled) {
            style.labelColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.selected && !props.disabled) {
            style.labelColor = scheme.basicTextOnAccent
        }
        if (props.selected && props.disabled) {
            style.labelColor = scheme.interactionStatesDisabledTextOnAccentDisabled
        }
        return style
    }
}
