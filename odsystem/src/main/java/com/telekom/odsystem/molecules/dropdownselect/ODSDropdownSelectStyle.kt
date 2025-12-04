package com.telekom.odsystem.molecules.dropdownselect

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import com.telekom.odsystem.componenttokens.DSDropdownSelectTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSOffset
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-08-01 (v1.32.3) - uid: aa83a39
 * Figma link: https://figma.com/design/9DAhZcVWjS2WXQjpYkC6D5/OneID Expl_Concept Topics?node-id=5982-57227
 */
@Suppress("All")
class ODSDropdownSelectStyle {
    var zStackContentAlignment: Alignment? = null // Not used in mobile
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var contentAlignment: Alignment? = null
    var inputFieldGap: Dp? = null
    var inputFieldBackground: List<ODSColorModel>? = null
    var inputFieldPadding: ODSPadding? = null
    var inputFieldCornerRadius: ODSCorners? = null
    var inputFieldBorder: Dp? = null
    var inputFieldBorderColor: List<ODSColorModel>? = null
    var inputFieldMinHeight: Dp? = null
    var inputFieldVerticalAlignment: Alignment.Vertical? = null
    var inputFieldHorizontalAlignment: Alignment.Horizontal? = null
    var inputFieldHorizontalArrangement: Arrangement.Horizontal? = null
    var contentGap: Dp? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentVerticalArrangement: Arrangement.Vertical? = null
    var eyebrowGap: Dp? = null
    var eyebrowVerticalAlignment: Alignment.Vertical? = null
    var eyebrowHorizontalAlignment: Alignment.Horizontal? = null
    var eyebrowHorizontalArrangement: Arrangement.Horizontal? = null
    var labelStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    var labelOverflow: TextOverflow? = null
    var labelMaxLines: Int? = null
    var requiredStyle: ODSTextStyle? = null
    var requiredColor: HexColor? = null
    var requiredTextAlign: TextAlign? = null // Not used cause of annotated string
    var inputGap: Dp? = null
    var inputMinHeight: Dp? = null
    var inputClipContent: Boolean? = null
    var inputVerticalAlignment: Alignment.Vertical? = null
    var inputHorizontalAlignment: Alignment.Horizontal? = null
    var inputHorizontalArrangement: Arrangement.Horizontal? = null
    var iconColor: HexColor? = null
    var iconWidth: Dp? = null
    var iconHeight: Dp? = null
    var valueStyle: ODSTextStyle? = null
    var valueColor: HexColor? = null
    var valueTextAlign: TextAlign? = null
    var valueOverflow: TextOverflow? = null
    var valueMaxLines: Int? = null
    var expandAndCollapseIconPadding: ODSPadding? = null
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
    var supportTextPadding: ODSPadding? = null
    var supportTextVerticalAlignment: Alignment.Vertical? = null
    var supportTextHorizontalAlignment: Alignment.Horizontal? = null
    var supportTextHorizontalArrangement: Arrangement.Horizontal? = null
    var flyoutContainerAbsoluteOffset: ODSOffset? = null // Not used in mobile
    var flyoutContainerAbsoluteContentAlignment: Alignment? = null // Not used in mobile
    var flyoutContainerVerticalAlignment: Alignment.Vertical? = null // Not used in mobile
    var flyoutContainerHorizontalAlignment: Alignment.Horizontal? = null // Not used in mobile
    var flyoutContainerHorizontalArrangement: Arrangement.Horizontal? = null // Not used in mobile
    var flyoutContainerVerticalArrangement: Arrangement.Vertical? = null // Not used in mobile
    var dropdownPadding: ODSPadding? = null // Not exported from the plugin
    var dropdownOffset: DpOffset? = null // Not exported from the plugin
    var dropdownBackgroundColor: HexColor? = null // Not exported from the plugin
    var dropdownBorderColor: HexColor? = null // Not exported from the plugin
    var dropdownBorderWidth: Dp? = null // Not exported from the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSDropdownSelectProps,
        state: ODSActions,
    ): ODSDropdownSelectStyle {
        val style = ODSDropdownSelectStyle()
        style.zStackContentAlignment = DSDropdownSelectTokens.zStackContentAlignment
        style.gap = DSDropdownSelectTokens.gap
        style.verticalAlignment = DSDropdownSelectTokens.verticalAlignment
        style.horizontalAlignment = DSDropdownSelectTokens.horizontalAlignment
        style.verticalArrangement = DSDropdownSelectTokens.verticalArrangement
        style.contentAlignment = DSDropdownSelectTokens.contentAlignment
        style.inputFieldPadding = DSDropdownSelectTokens.inputFieldPadding
        style.inputFieldCornerRadius = DSDropdownSelectTokens.inputFieldCornerRadius
        style.inputFieldVerticalAlignment = DSDropdownSelectTokens.inputFieldVerticalAlignment
        style.inputFieldHorizontalAlignment = DSDropdownSelectTokens.inputFieldHorizontalAlignment
        style.inputFieldHorizontalArrangement =
            DSDropdownSelectTokens.inputFieldHorizontalArrangement
        if (props.size == ODSDropdownSelectSize.LARGE) {
            style.inputFieldGap = DSDropdownSelectTokens.inputFieldGapSizeLarge
            style.inputFieldMinHeight = DSDropdownSelectTokens.inputFieldMinHeightSizeLarge
        }
        if (props.size == ODSDropdownSelectSize.SMALL) {
            style.inputFieldGap = DSDropdownSelectTokens.inputFieldGapSizeSmall
            style.inputFieldMinHeight = DSDropdownSelectTokens.inputFieldMinHeightSizeSmall
        }
        if (props.mode == ODSDropdownSelectMode.STANDARD) {
            style.inputFieldBorder = DSDropdownSelectTokens.inputFieldBorderModeStandard
        }
        if (props.mode == ODSDropdownSelectMode.INFORMATIVE) {
            style.inputFieldBorder = DSDropdownSelectTokens.inputFieldBorderModeInformative
        }
        if (!props.disabled && !props.readOnly) {
            style.inputFieldBackground = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        }
        if (props.mode == ODSDropdownSelectMode.ERROR && !props.disabled && !props.readOnly) {
            style.inputFieldBorder = DSDropdownSelectTokens.inputFieldBorderModeError
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
        }
        if (props.mode == ODSDropdownSelectMode.INFORMATIVE && !props.disabled && !props.readOnly) {
            style.inputFieldBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.mode == ODSDropdownSelectMode.STANDARD && !props.disabled && !props.readOnly) {
            style.inputFieldBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (!props.selected && props.mode == ODSDropdownSelectMode.INFORMATIVE && props.disabled && !props.expanded) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (!props.selected && props.mode == ODSDropdownSelectMode.STANDARD && props.disabled && !props.expanded) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.selected && props.mode == ODSDropdownSelectMode.INFORMATIVE && !props.disabled && props.readOnly && !props.expanded) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.selected && props.mode == ODSDropdownSelectMode.STANDARD && !props.disabled && props.readOnly && !props.expanded) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (!props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        if (!props.disabled && !props.readOnly && state == ODSActions.PRESSED) {
            style.inputFieldBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundPressed))
        }
        if (props.mode == ODSDropdownSelectMode.INFORMATIVE && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverStrokeHover))
        }
        if (props.mode == ODSDropdownSelectMode.STANDARD && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverStrokeHover))
        }
        if (props.mode == ODSDropdownSelectMode.INFORMATIVE && !props.disabled && !props.readOnly && state == ODSActions.PRESSED) {
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedStrokePressed))
        }
        if (props.mode == ODSDropdownSelectMode.STANDARD && !props.disabled && !props.readOnly && state == ODSActions.PRESSED) {
            style.inputFieldBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedStrokePressed))
        }
        style.contentVerticalAlignment = DSDropdownSelectTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSDropdownSelectTokens.contentHorizontalAlignment
        style.contentVerticalArrangement = DSDropdownSelectTokens.contentVerticalArrangement
        if (props.size == ODSDropdownSelectSize.LARGE) {
            style.contentGap = DSDropdownSelectTokens.contentGapSizeLarge
        }
        style.eyebrowGap = DSDropdownSelectTokens.eyebrowGap
        style.eyebrowHorizontalAlignment = DSDropdownSelectTokens.eyebrowHorizontalAlignment
        style.eyebrowHorizontalArrangement = DSDropdownSelectTokens.eyebrowHorizontalArrangement
        if (!props.selected && !props.readOnly) {
            style.eyebrowVerticalAlignment = DSDropdownSelectTokens.eyebrowVerticalAlignment
        }
        if (props.selected && !props.disabled) {
            style.eyebrowVerticalAlignment = DSDropdownSelectTokens.eyebrowVerticalAlignmentSelected
        }
        style.labelTextAlign = DSDropdownSelectTokens.labelTextAlign
        style.labelOverflow = DSDropdownSelectTokens.labelOverflow
        style.labelMaxLines = DSDropdownSelectTokens.labelMaxLines
        if (!props.disabled) {
            style.labelColor = scheme.basicTextRecessive
        }
        if (props.size == ODSDropdownSelectSize.SMALL && !props.selected) {
            style.labelStyle = DSDropdownSelectTokens.labelStyleSizeSmall
        }
        if (props.size == ODSDropdownSelectSize.LARGE && props.selected) {
            style.labelStyle = DSDropdownSelectTokens.labelStyleSizeLargeSelected
        }
        if (props.size == ODSDropdownSelectSize.LARGE && !props.selected && !props.readOnly) {
            style.labelStyle = DSDropdownSelectTokens.labelStyleSizeLarge
        }
        if (props.size == ODSDropdownSelectSize.SMALL && props.selected && !props.disabled) {
            style.labelStyle = DSDropdownSelectTokens.labelStyleSizeSmallSelected
        }
        if (!props.selected && props.mode == ODSDropdownSelectMode.INFORMATIVE && props.disabled && !props.readOnly && !props.expanded) {
            style.labelColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (!props.selected && props.mode == ODSDropdownSelectMode.STANDARD && props.disabled && !props.readOnly && !props.expanded) {
            style.labelColor = scheme.interactionStatesDisabledTextDisabled
        }
        style.requiredTextAlign = DSDropdownSelectTokens.requiredTextAlign
        if (!props.disabled) {
            style.requiredColor = scheme.basicTextRecessive
        }
        if (props.size == ODSDropdownSelectSize.SMALL && !props.selected) {
            style.requiredStyle = DSDropdownSelectTokens.requiredStyleSizeSmall
        }
        if (props.size == ODSDropdownSelectSize.LARGE && props.selected) {
            style.requiredStyle = DSDropdownSelectTokens.requiredStyleSizeLargeSelected
        }
        if (props.size == ODSDropdownSelectSize.LARGE && !props.selected && !props.readOnly) {
            style.requiredStyle = DSDropdownSelectTokens.requiredStyleSizeLarge
        }
        if (props.size == ODSDropdownSelectSize.SMALL && props.selected && !props.disabled) {
            style.requiredStyle = DSDropdownSelectTokens.requiredStyleSizeSmallSelected
        }
        if (!props.selected && props.mode == ODSDropdownSelectMode.INFORMATIVE && props.disabled && !props.readOnly && !props.expanded) {
            style.requiredColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        if (!props.selected && props.mode == ODSDropdownSelectMode.STANDARD && props.disabled && !props.readOnly && !props.expanded) {
            style.requiredColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        style.inputGap = DSDropdownSelectTokens.inputGap
        style.inputMinHeight = DSDropdownSelectTokens.inputMinHeight
        style.inputClipContent = DSDropdownSelectTokens.inputClipContent
        style.inputVerticalAlignment = DSDropdownSelectTokens.inputVerticalAlignment
        style.inputHorizontalAlignment = DSDropdownSelectTokens.inputHorizontalAlignment
        style.inputHorizontalArrangement = DSDropdownSelectTokens.inputHorizontalArrangement
        style.iconColor = scheme.basicTextRecessive
        style.iconWidth = DSDropdownSelectTokens.iconWidth
        style.iconHeight = DSDropdownSelectTokens.iconHeight
        style.valueColor = scheme.basicText
        style.valueTextAlign = DSDropdownSelectTokens.valueTextAlign
        style.valueOverflow = DSDropdownSelectTokens.valueOverflow
        style.valueMaxLines = DSDropdownSelectTokens.valueMaxLines
        if (props.size == ODSDropdownSelectSize.LARGE) {
            style.valueStyle = DSDropdownSelectTokens.valueStyleSizeLarge
        }
        if (props.size == ODSDropdownSelectSize.SMALL) {
            style.valueStyle = DSDropdownSelectTokens.valueStyleSizeSmall
        }
        style.expandAndCollapseIconPadding = DSDropdownSelectTokens.expandAndCollapseIconPadding
        style.expandAndCollapseIconWidth = DSDropdownSelectTokens.expandAndCollapseIconWidth
        style.expandAndCollapseIconHeight = DSDropdownSelectTokens.expandAndCollapseIconHeight
        style.expandAndCollapseIconClipContent =
            DSDropdownSelectTokens.expandAndCollapseIconClipContent
        style.expandAndCollapseIconVerticalAlignment =
            DSDropdownSelectTokens.expandAndCollapseIconVerticalAlignment
        style.expandAndCollapseIconHorizontalAlignment =
            DSDropdownSelectTokens.expandAndCollapseIconHorizontalAlignment
        style.expandAndCollapseIconHorizontalArrangement =
            DSDropdownSelectTokens.expandAndCollapseIconHorizontalArrangement
        if (props.size == ODSDropdownSelectSize.LARGE) {
            style.collapseDownWidth = DSDropdownSelectTokens.collapseDownWidthSizeLarge
            style.collapseDownHeight = DSDropdownSelectTokens.collapseDownHeightSizeLarge
        }
        if (props.size == ODSDropdownSelectSize.SMALL) {
            style.collapseDownWidth = DSDropdownSelectTokens.collapseDownWidthSizeSmall
            style.collapseDownHeight = DSDropdownSelectTokens.collapseDownHeightSizeSmall
        }
        if (!props.disabled && !props.readOnly) {
            style.collapseDownColor = scheme.basicTextRecessive
        }
        if (!props.selected && props.mode == ODSDropdownSelectMode.INFORMATIVE && props.disabled && !props.readOnly && !props.expanded) {
            style.collapseDownColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (!props.selected && props.mode == ODSDropdownSelectMode.STANDARD && props.disabled && !props.readOnly && !props.expanded) {
            style.collapseDownColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.selected && props.mode == ODSDropdownSelectMode.INFORMATIVE && !props.disabled && props.readOnly && !props.expanded) {
            style.collapseDownColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        if (props.selected && props.mode == ODSDropdownSelectMode.STANDARD && !props.disabled && props.readOnly && !props.expanded) {
            style.collapseDownColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        style.collapseUpColor = scheme.basicTextRecessive
        if (props.size == ODSDropdownSelectSize.LARGE) {
            style.collapseUpWidth = DSDropdownSelectTokens.collapseUpWidthSizeLarge
            style.collapseUpHeight = DSDropdownSelectTokens.collapseUpHeightSizeLarge
        }
        if (props.size == ODSDropdownSelectSize.SMALL) {
            style.collapseUpWidth = DSDropdownSelectTokens.collapseUpWidthSizeSmall
            style.collapseUpHeight = DSDropdownSelectTokens.collapseUpHeightSizeSmall
        }
        style.supportTextPadding = DSDropdownSelectTokens.supportTextPadding
        style.supportTextVerticalAlignment = DSDropdownSelectTokens.supportTextVerticalAlignment
        style.supportTextHorizontalAlignment = DSDropdownSelectTokens.supportTextHorizontalAlignment
        style.supportTextHorizontalArrangement =
            DSDropdownSelectTokens.supportTextHorizontalArrangement
        style.flyoutContainerAbsoluteContentAlignment =
            DSDropdownSelectTokens.flyoutContainerAbsoluteContentAlignment
        if (props.size == ODSDropdownSelectSize.LARGE) {
            style.flyoutContainerAbsoluteOffset =
                DSDropdownSelectTokens.flyoutContainerAbsoluteOffsetSizeLarge
        }
        if (props.size == ODSDropdownSelectSize.SMALL) {
            style.flyoutContainerAbsoluteOffset =
                DSDropdownSelectTokens.flyoutContainerAbsoluteOffsetSizeSmall
        }
        style.flyoutContainerVerticalAlignment =
            DSDropdownSelectTokens.flyoutContainerVerticalAlignment
        style.flyoutContainerHorizontalAlignment =
            DSDropdownSelectTokens.flyoutContainerHorizontalAlignment
        if (!props.expanded) {
            style.flyoutContainerHorizontalArrangement =
                DSDropdownSelectTokens.flyoutContainerHorizontalArrangement
        }
        if (props.selected && props.mode == ODSDropdownSelectMode.ERROR && !props.disabled && !props.readOnly && props.expanded) {
            style.flyoutContainerVerticalArrangement =
                DSDropdownSelectTokens.flyoutContainerVerticalArrangementSelectedModeErrorExpanded
        }
        if (props.selected && props.mode == ODSDropdownSelectMode.INFORMATIVE && !props.disabled && !props.readOnly && props.expanded) {
            style.flyoutContainerVerticalArrangement =
                DSDropdownSelectTokens.flyoutContainerVerticalArrangementSelectedModeInformativeExpanded
        }
        if (props.selected && props.mode == ODSDropdownSelectMode.STANDARD && !props.disabled && !props.readOnly && props.expanded) {
            style.flyoutContainerVerticalArrangement =
                DSDropdownSelectTokens.flyoutContainerVerticalArrangementSelectedModeStandardExpanded
        }
        if (!props.selected && props.mode == ODSDropdownSelectMode.ERROR && !props.disabled && !props.readOnly && props.expanded) {
            style.flyoutContainerVerticalArrangement =
                DSDropdownSelectTokens.flyoutContainerVerticalArrangementModeErrorExpanded
        }
        if (!props.selected && props.mode == ODSDropdownSelectMode.INFORMATIVE && !props.disabled && !props.readOnly && props.expanded) {
            style.flyoutContainerVerticalArrangement =
                DSDropdownSelectTokens.flyoutContainerVerticalArrangementModeInformativeExpanded
        }
        if (!props.selected && props.mode == ODSDropdownSelectMode.STANDARD && !props.disabled && !props.readOnly && props.expanded) {
            style.flyoutContainerVerticalArrangement =
                DSDropdownSelectTokens.flyoutContainerVerticalArrangementModeStandardExpanded
        }

        // Custom addition
        if (props.size == ODSDropdownSelectSize.LARGE) {
            style.dropdownPadding =
                DSDropdownSelectTokens.dropdownPaddingSizeLarge
        }
        if (props.size == ODSDropdownSelectSize.SMALL) {
            style.dropdownPadding =
                DSDropdownSelectTokens.dropdownPaddingSizeSmall
            style.contentGap = DSDropdownSelectTokens.contentGapSizeLarge
        }
        style.dropdownOffset = DSDropdownSelectTokens.dropdownOffset
        style.dropdownBackgroundColor = scheme.basicBackground
        style.dropdownBorderColor = scheme.basicStroke
        style.dropdownBorderWidth =
            DSDropdownSelectTokens.dropdownBorderWidth
        return style
    }
}
