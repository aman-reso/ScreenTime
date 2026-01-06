package com.telekom.odsystem.atoms.tabitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSTabItemTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSTabItemStyle {
    var minHeight: Dp? = null
    var minWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var contentFrameGap: Dp? = null
    var contentFramePadding: ODSPadding? = null
    var contentFrameVerticalAlignment: Alignment.Vertical? = null
    var contentFrameHorizontalAlignment: Alignment.Horizontal? = null
    var contentFrameHorizontalArrangement: Arrangement.Horizontal? = null
    var iconColor: HexColor? = null
    var iconWidth: Dp? = null
    var iconHeight: Dp? = null
    var labelTextStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    var lineContainerBorderRadius: ODSCorners? = null // Not used in mobile
    var lineContainerHeight: Dp? = null // Not used in mobile
    var lineContainerVerticalAlignment: Alignment.Vertical? = null // Not used in mobile
    var lineContainerHorizontalAlignment: Alignment.Horizontal? = null // Not used in mobile
    var lineContainerHorizontalArrangement: Arrangement.Horizontal? = null // Not used in mobile
    var lineContainerBackgroundColor: List<ODSColorModel>? = null // Not used in mobile
    fun getStyle(
        scheme: ODSTheme,
        props: ODSTabItemProps,
        state: ODSActions
    ): ODSTabItemStyle {
        val style = ODSTabItemStyle()
        style.minWidth = DSTabItemTokens.minWidth
        style.verticalAlignment = DSTabItemTokens.verticalAlignment
        style.horizontalAlignment = DSTabItemTokens.horizontalAlignment
        style.verticalArrangement = DSTabItemTokens.verticalArrangement
        if (props.size == ODSTabItemSize.LARGE) {
            style.minHeight = DSTabItemTokens.minHeightSizeLarge
        }
        if (props.size == ODSTabItemSize.SMALL) {
            style.minHeight = DSTabItemTokens.minHeightSizeSmall
        }
        style.contentFramePadding = DSTabItemTokens.contentFramePadding
        style.contentFrameVerticalAlignment = DSTabItemTokens.contentFrameVerticalAlignment
        style.contentFrameHorizontalAlignment = DSTabItemTokens.contentFrameHorizontalAlignment
        style.contentFrameHorizontalArrangement = DSTabItemTokens.contentFrameHorizontalArrangement
        if (props.size == ODSTabItemSize.LARGE) {
            style.contentFrameGap = DSTabItemTokens.contentFrameGapSizeLarge
        }
        if (props.size == ODSTabItemSize.SMALL) {
            style.contentFrameGap = DSTabItemTokens.contentFrameGapSizeSmall
        }
        if (props.selected) {
            style.iconColor = scheme.basicAccent
        }
        if (!props.selected) {
            style.iconColor = scheme.basicAccentSecondary
        }
        if (state == ODSActions.HOVERED) {
            style.iconColor = scheme.interactionStatesHoverAccentHover
        }
        if (state == ODSActions.PRESSED) {
            style.iconColor = scheme.interactionStatesPressedAccentPressed
        }
        if (props.size == ODSTabItemSize.LARGE) {
            style.iconWidth = DSTabItemTokens.iconWidthSizeLarge
            style.iconHeight = DSTabItemTokens.iconHeightSizeLarge
        }
        if (props.size == ODSTabItemSize.SMALL) {
            style.iconWidth = DSTabItemTokens.iconWidthSizeSmall
            style.iconHeight = DSTabItemTokens.iconHeightSizeSmall
        }
        style.labelTextAlign = DSTabItemTokens.labelTextAlign
        if (props.selected) {
            style.labelColor = scheme.basicAccentSecondary
        }
        if (!props.selected) {
            style.labelColor = scheme.basicTextRecessive
        }
        if (state == ODSActions.HOVERED) {
            style.labelColor = scheme.interactionStatesHoverTextDominantHover
        }
        if (state == ODSActions.PRESSED) {
            style.labelColor = scheme.interactionStatesPressedTextDominantPressed
        }
        if (props.size == ODSTabItemSize.LARGE) {
            style.labelTextStyle = DSTabItemTokens.labelTextStyleSizeLarge
        }
        if (props.size == ODSTabItemSize.SMALL) {
            style.labelTextStyle = DSTabItemTokens.labelTextStyleSizeSmall
        }
        style.lineContainerBorderRadius = DSTabItemTokens.lineContainerBorderRadius
        style.lineContainerHeight = DSTabItemTokens.lineContainerHeight
        style.lineContainerVerticalAlignment = DSTabItemTokens.lineContainerVerticalAlignment
        style.lineContainerHorizontalAlignment = DSTabItemTokens.lineContainerHorizontalAlignment
        style.lineContainerHorizontalArrangement =
            DSTabItemTokens.lineContainerHorizontalArrangement
        if (props.selected) {
            style.lineContainerBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
        }
        if (state == ODSActions.HOVERED && props.selected) {
            style.lineContainerBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverAccentHover))
        }
        if (state == ODSActions.PRESSED && props.selected) {
            style.lineContainerBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedAccentPressed))
        }
        return style
    }
}
