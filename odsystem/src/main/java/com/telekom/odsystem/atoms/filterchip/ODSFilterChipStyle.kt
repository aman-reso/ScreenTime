package com.telekom.odsystem.atoms.filterchip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import com.telekom.odsystem.componenttokens.DSFilterChipTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSOffset
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSFilterChipStyle {
    var zStackMinHeight: Dp? = null // Not used in mobile
    var zStackMinWidth: Dp? = null // Not used in mobile
    var zStackContentAlignment: Alignment? = null // Not used in mobile
    var minHeight: Dp? = null
    var minWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var contentAlignment: Alignment? = null // Not used in mobile
    var filterChipGap: Dp? = null
    var filterChipBackground: List<ODSColorModel>? = null
    var filterChipPadding: ODSPadding? = null
    var filterChipCornerRadius: ODSCorners? = null
    var filterChipBorder: Dp? = null
    var filterChipBorderColor: List<ODSColorModel>? = null
    var filterChipMinHeight: Dp? = null
    var filterChipMinWidth: Dp? = null
    var filterChipVerticalAlignment: Alignment.Vertical? = null
    var filterChipHorizontalAlignment: Alignment.Horizontal? = null
    var filterChipHorizontalArrangement: Arrangement.Horizontal? = null
    var filterStyle: ODSTextStyle? = null
    var filterColor: HexColor? = null
    var filterTextAlign: TextAlign? = null
    var filterOverflow: TextOverflow? = null
    var collapseDownColor: HexColor? = null
    var collapseDownWidth: Dp? = null
    var collapseDownHeight: Dp? = null
    var collapseUpColor: HexColor? = null
    var collapseUpWidth: Dp? = null
    var collapseUpHeight: Dp? = null
    var odsChipFilterListContainerAbsoluteContentAlignment: Alignment? = null // Not used in mobile
    var odsChipFilterListContainerAbsoluteOffset: ODSOffset? = null // Not used in mobile
    var dropdownBorderRadius: ODSCorners? = null // Not exported from the plugin
    var dropdownPadding: ODSPadding? = null // Not exported from the plugin
    var dropdownOffset: DpOffset? = null // Not exported from the plugin
    var dropdownBackgroundColor: HexColor? = null // Not exported from the plugin
    var dropdownBorderColor: HexColor? = null // Not exported from the plugin
    var dropdownBorderWidth: Dp? = null // Not exported from the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSFilterChipProps,
        state: ODSActions
    ): ODSFilterChipStyle {
        val style = ODSFilterChipStyle()
        style.zStackMinHeight = DSFilterChipTokens.zStackMinHeight
        style.zStackMinWidth = DSFilterChipTokens.zStackMinWidth
        if (props.expanded && !props.disabled) {
            style.zStackContentAlignment = DSFilterChipTokens.zStackContentAlignmentExpanded
        }
        style.minHeight = DSFilterChipTokens.minHeight
        style.minWidth = DSFilterChipTokens.minWidth
        style.verticalAlignment = DSFilterChipTokens.verticalAlignment
        style.horizontalAlignment = DSFilterChipTokens.horizontalAlignment
        style.verticalArrangement = DSFilterChipTokens.verticalArrangement
        if (props.expanded && !props.disabled) {
            style.contentAlignment = DSFilterChipTokens.contentAlignmentExpanded
        }
        style.filterChipGap = DSFilterChipTokens.filterChipGap
        style.filterChipPadding = DSFilterChipTokens.filterChipPadding
        style.filterChipCornerRadius = DSFilterChipTokens.filterChipCornerRadius
        style.filterChipMinHeight = DSFilterChipTokens.filterChipMinHeight
        style.filterChipMinWidth = DSFilterChipTokens.filterChipMinWidth
        style.filterChipVerticalAlignment = DSFilterChipTokens.filterChipVerticalAlignment
        style.filterChipHorizontalAlignment = DSFilterChipTokens.filterChipHorizontalAlignment
        style.filterChipHorizontalArrangement = DSFilterChipTokens.filterChipHorizontalArrangement
        if (props.selectedValue == null) {
            style.filterChipBorder = DSFilterChipTokens.filterChipBorder
        }
        if (props.selectedValue == null && !props.disabled) {
            style.filterChipBackground = listOf(ODSColorModel(hexColor = scheme.basicBackground))
            style.filterChipBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.selectedValue != null && !props.disabled) {
            style.filterChipBackground =
                listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
        }
        if (props.selectedValue == null && !props.disabled && state == ODSActions.HOVERED) {
            style.filterChipBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
            style.filterChipBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverStrokeHover))
        }
        if (props.selectedValue != null && !props.disabled && state == ODSActions.HOVERED) {
            style.filterChipBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverAccentSecondaryHover))
        }
        if (props.selectedValue == null && !props.disabled && state == ODSActions.PRESSED) {
            style.filterChipBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundPressed))
            style.filterChipBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedStrokePressed))
        }
        if (props.selectedValue != null && !props.disabled && state == ODSActions.PRESSED) {
            style.filterChipBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedAccentSecondaryPressed))
        }
        if (!props.expanded && props.selectedValue == null && props.disabled) {
            style.filterChipBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.filterChipBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (!props.expanded && props.selectedValue != null && props.disabled) {
            style.filterChipBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledAccentSecondaryDisabled))
        }
        style.filterStyle = DSFilterChipTokens.filterStyle
        style.filterTextAlign = DSFilterChipTokens.filterTextAlign
        if (!props.expanded) {
            style.filterOverflow = DSFilterChipTokens.filterOverflow
        }
        if (props.selectedValue == null && !props.disabled) {
            style.filterColor = scheme.basicText
        }
        if (props.selectedValue != null && !props.disabled) {
            style.filterColor = scheme.basicTextOnAccentSecondary
        }
        if (props.expanded && props.disabled) {
            style.filterOverflow = DSFilterChipTokens.filterOverflowExpandedDisabled
        }
        if (props.expanded && !props.disabled) {
            style.filterOverflow = DSFilterChipTokens.filterOverflowExpanded
        }
        if (!props.expanded && props.selectedValue == null && props.disabled) {
            style.filterColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (!props.expanded && props.selectedValue != null && props.disabled) {
            style.filterColor = scheme.interactionStatesDisabledTextOnAccentSecondaryDisabled
        }
        if (props.expanded && !props.disabled && state == ODSActions.PRESSED) {
            style.filterOverflow = DSFilterChipTokens.filterOverflowExpandedStatePressed
        }
        if (!props.expanded) {
            style.collapseDownWidth = DSFilterChipTokens.collapseDownWidth
            style.collapseDownHeight = DSFilterChipTokens.collapseDownHeight
        }
        if (!props.expanded && props.selectedValue == null && !props.disabled) {
            style.collapseDownColor = scheme.basicText
        }
        if (!props.expanded && props.selectedValue != null && !props.disabled) {
            style.collapseDownColor = scheme.basicTextOnAccentSecondary
        }
        if (!props.expanded && props.selectedValue == null && props.disabled) {
            style.collapseDownColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (!props.expanded && props.selectedValue != null && props.disabled) {
            style.collapseDownColor = scheme.interactionStatesDisabledTextOnAccentSecondaryDisabled
        }
        if (props.expanded && !props.disabled) {
            style.collapseUpWidth = DSFilterChipTokens.collapseUpWidthExpanded
            style.collapseUpHeight = DSFilterChipTokens.collapseUpHeightExpanded
        }
        if (props.expanded && props.selectedValue == null && !props.disabled) {
            style.collapseUpColor = scheme.basicText
        }
        if (props.expanded && props.selectedValue != null && !props.disabled) {
            style.collapseUpColor = scheme.basicTextOnAccentSecondary
        }
        if (props.expanded && !props.disabled) {
            style.odsChipFilterListContainerAbsoluteContentAlignment =
                DSFilterChipTokens.odsChipFilterListContainerAbsoluteContentAlignmentExpanded
            style.odsChipFilterListContainerAbsoluteOffset =
                DSFilterChipTokens.odsChipFilterListContainerAbsoluteOffsetExpanded
        }

        // Custom addition
        style.dropdownBorderRadius = DSFilterChipTokens.dropdownBorderRadius
        style.dropdownPadding = DSFilterChipTokens.dropdownPadding
        style.dropdownOffset = DSFilterChipTokens.dropdownOffset
        style.dropdownBackgroundColor = scheme.basicBackground
        style.dropdownBorderColor = scheme.basicStroke
        style.dropdownBorderWidth = DSFilterChipTokens.dropdownBorderWidth
        return style
    }
}
