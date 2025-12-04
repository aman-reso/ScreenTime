package com.telekom.odsystem.molecules.accordion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.tokens.componenttokens.DSAccordionTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
/**
 * Code generated with ODS RADD Code Generator Plugin
 * 2025-06-23 (v1.31.6) - uid: 427e2ad2
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=4627-4153
 */

class ODSAccordionStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var headerIconGap: Dp? = null
    var headerIconPadding: ODSPadding? = null
    var headerIconCornerRadius: ODSCorners? = null
    var headerIconMinHeight: Dp? = null
    var headerIconVerticalAlignment: Alignment.Vertical? = null
    var headerIconHorizontalAlignment: Alignment.Horizontal? = null
    var headerIconHorizontalArrangement: Arrangement.Horizontal? = null
    var headerIconBackground: List<ODSColorModel>? = null
    var headerStyle: ODSTextStyle? = null
    var headerColor: HexColor? = null
    var headerTextAlign: TextAlign? = null
    var expandAndCollapseIconWidth: Dp? = null
    var expandAndCollapseIconHeight: Dp? = null
    var expandAndCollapseIconClipContent: Boolean? = null
    var expandAndCollapseIconVerticalAlignment: Alignment.Vertical? = null
    var expandAndCollapseIconHorizontalAlignment: Alignment.Horizontal? = null
    var expandAndCollapseIconHorizontalArrangement: Arrangement.Horizontal? = null
    var collapseDownColor: HexColor? = null
    var collapseDownWidth: Dp? = null
    var collapseDownHeight: Dp? = null
    var collapseUpColor: HexColor? = null
    var collapseUpWidth: Dp? = null
    var collapseUpHeight: Dp? = null
    var contentFrameVerticalAlignment: Alignment.Vertical? = null
    var contentFrameHorizontalAlignment: Alignment.Horizontal? = null
    var contentFrameVerticalArrangement: Arrangement.Vertical? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSAccordionProps,
        state: ODSActions
    ): ODSAccordionStyle {
        val style = ODSAccordionStyle()
        style.gap = DSAccordionTokens.gap
        style.verticalAlignment = DSAccordionTokens.verticalAlignment
        style.horizontalAlignment = DSAccordionTokens.horizontalAlignment
        style.verticalArrangement = DSAccordionTokens.verticalArrangement
        style.headerIconGap = DSAccordionTokens.headerIconGap
        style.headerIconCornerRadius = DSAccordionTokens.headerIconCornerRadius
        style.headerIconVerticalAlignment = DSAccordionTokens.headerIconVerticalAlignment
        style.headerIconHorizontalAlignment = DSAccordionTokens.headerIconHorizontalAlignment
        style.headerIconHorizontalArrangement = DSAccordionTokens.headerIconHorizontalArrangement
        if (props.size == ODSAccordionSize.LARGE) {
            style.headerIconPadding = DSAccordionTokens.headerIconPaddingSizeLarge
            style.headerIconMinHeight = DSAccordionTokens.headerIconMinHeightSizeLarge
        }
        if (props.size == ODSAccordionSize.SMALL) {
            style.headerIconPadding = DSAccordionTokens.headerIconPaddingSizeSmall
            style.headerIconMinHeight = DSAccordionTokens.headerIconMinHeightSizeSmall
        }
        if (!props.disabled && state == ODSActions.HOVERED) {
            style.headerIconBackground = listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        if (!props.disabled && state == ODSActions.PRESSED) {
            style.headerIconBackground = listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundPressed))
        }
        if (!props.expanded && props.disabled) {
            style.headerIconBackground = listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
        }
        style.headerTextAlign = DSAccordionTokens.headerTextAlign
        if (!props.disabled) {
            style.headerColor = scheme.basicText
        }
        if (props.size == ODSAccordionSize.LARGE) {
            style.headerStyle = DSAccordionTokens.headerStyleSizeLarge
        }
        if (props.size == ODSAccordionSize.SMALL) {
            style.headerStyle = DSAccordionTokens.headerStyleSizeSmall
        }
        if (!props.expanded && props.disabled) {
            style.headerColor = scheme.interactionStatesDisabledTextDisabled
        }
        style.expandAndCollapseIconWidth = DSAccordionTokens.expandAndCollapseIconWidth
        style.expandAndCollapseIconHeight = DSAccordionTokens.expandAndCollapseIconHeight
        style.expandAndCollapseIconClipContent = DSAccordionTokens.expandAndCollapseIconClipContent
        style.expandAndCollapseIconVerticalAlignment = DSAccordionTokens.expandAndCollapseIconVerticalAlignment
        style.expandAndCollapseIconHorizontalAlignment = DSAccordionTokens.expandAndCollapseIconHorizontalAlignment
        style.expandAndCollapseIconHorizontalArrangement = DSAccordionTokens.expandAndCollapseIconHorizontalArrangement
        if (!props.expanded && !props.disabled) {
            style.collapseDownColor = scheme.basicText
        }
        if (!props.expanded && props.disabled) {
            style.collapseDownColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.size == ODSAccordionSize.LARGE && !props.expanded) {
            style.collapseDownWidth = DSAccordionTokens.collapseDownWidthSizeLarge
            style.collapseDownHeight = DSAccordionTokens.collapseDownHeightSizeLarge
        }
        if (props.size == ODSAccordionSize.SMALL && !props.expanded) {
            style.collapseDownWidth = DSAccordionTokens.collapseDownWidthSizeSmall
            style.collapseDownHeight = DSAccordionTokens.collapseDownHeightSizeSmall
        }
        if (props.expanded && !props.disabled) {
            style.collapseUpColor = scheme.basicText
        }
        if (props.size == ODSAccordionSize.LARGE && props.expanded && !props.disabled) {
            style.collapseUpWidth = DSAccordionTokens.collapseUpWidthSizeLargeExpanded
            style.collapseUpHeight = DSAccordionTokens.collapseUpHeightSizeLargeExpanded
        }
        if (props.size == ODSAccordionSize.SMALL && props.expanded && !props.disabled) {
            style.collapseUpWidth = DSAccordionTokens.collapseUpWidthSizeSmallExpanded
            style.collapseUpHeight = DSAccordionTokens.collapseUpHeightSizeSmallExpanded
        }
        style.contentFrameVerticalAlignment = DSAccordionTokens.contentFrameVerticalAlignment
        style.contentFrameHorizontalAlignment = DSAccordionTokens.contentFrameHorizontalAlignment
        style.contentFrameVerticalArrangement = DSAccordionTokens.contentFrameVerticalArrangement
        return style
    }
}
