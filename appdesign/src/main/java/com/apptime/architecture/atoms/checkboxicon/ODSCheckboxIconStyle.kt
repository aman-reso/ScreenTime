package com.telekom.odsystem.atoms.checkboxicon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSCheckboxIconTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("All")
/**
 * Code generated with ODS RADD Code Generator Plugin
 * 2025-06-16 (v1.31.6) - uid: 4592d91b
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=15509-8520
 */

class ODSCheckboxIconStyle {
    var padding: ODSPadding? = null
    var width: Dp? = null
    var height: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var iconBackground: List<ODSColorModel>? = null
    var iconCornerRadius: ODSCorners? = null
    var iconVerticalAlignment: Alignment.Vertical? = null
    var iconHorizontalAlignment: Alignment.Horizontal? = null
    var iconHorizontalArrangement: Arrangement.Horizontal? = null
    var iconBorder: Dp? = null
    var iconBorderColor: List<ODSColorModel>? = null
    var minusColor: HexColor? = null
    var minusWidth: Dp? = null
    var minusHeight: Dp? = null
    var checkmarkColor: HexColor? = null
    var checkmarkWidth: Dp? = null
    var checkmarkHeight: Dp? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSCheckboxIconProps,
        state: ODSActions
    ): ODSCheckboxIconStyle {
        val style = ODSCheckboxIconStyle()
        style.padding = DSCheckboxIconTokens.padding
        style.verticalAlignment = DSCheckboxIconTokens.verticalAlignment
        style.horizontalAlignment = DSCheckboxIconTokens.horizontalAlignment
        style.horizontalArrangement = DSCheckboxIconTokens.horizontalArrangement
        if (state == ODSActions.PRESSED) {
            style.padding = DSCheckboxIconTokens.paddingStatePressed
        }
        if (props.size == ODSCheckboxIconSize.SMALL) {
            style.width = DSCheckboxIconTokens.widthSizeSmall
            style.height = DSCheckboxIconTokens.heightSizeSmall
        }
        if (props.size == ODSCheckboxIconSize.LARGE) {
            style.width = DSCheckboxIconTokens.widthSizeLarge
            style.height = DSCheckboxIconTokens.heightSizeLarge
        }
        if (!props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.padding = DSCheckboxIconTokens.paddingStateHovered
        }
        style.iconVerticalAlignment = DSCheckboxIconTokens.iconVerticalAlignment
        style.iconHorizontalAlignment = DSCheckboxIconTokens.iconHorizontalAlignment
        style.iconHorizontalArrangement = DSCheckboxIconTokens.iconHorizontalArrangement
        if (props.size == ODSCheckboxIconSize.SMALL) {
            style.iconCornerRadius = DSCheckboxIconTokens.iconCornerRadiusSizeSmall
        }
        if (props.size == ODSCheckboxIconSize.LARGE) {
            style.iconCornerRadius = DSCheckboxIconTokens.iconCornerRadiusSizeLarge
        }
        if (props.selected == ODSCheckboxIconSelected.UNSELECTED && !props.error) {
            style.iconBorder = DSCheckboxIconTokens.iconBorderSelectedUnselected
        }
        if (props.selected == ODSCheckboxIconSelected.INDETERMINATE && !props.error && props.disabled) {
            style.iconBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledAccentDisabled))
        }
        if (props.selected == ODSCheckboxIconSelected.SELECTED && !props.error && props.disabled) {
            style.iconBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledAccentDisabled))
        }
        if (props.selected == ODSCheckboxIconSelected.UNSELECTED && !props.error && props.disabled) {
            style.iconBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.iconBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.selected == ODSCheckboxIconSelected.UNSELECTED && !props.disabled && !props.readOnly) {
            style.iconBackground = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        }
        if (props.selected == ODSCheckboxIconSelected.INDETERMINATE && !props.error && !props.disabled && props.readOnly) {
            style.iconBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledAccentDisabled))
        }
        if (props.selected == ODSCheckboxIconSelected.SELECTED && !props.error && !props.disabled && props.readOnly) {
            style.iconBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledAccentDisabled))
        }
        if (props.selected == ODSCheckboxIconSelected.INDETERMINATE && !props.error && !props.disabled && !props.readOnly) {
            style.iconBackground = listOf(ODSColorModel(hexColor = scheme.basicAccent))
        }
        if (props.selected == ODSCheckboxIconSelected.SELECTED && !props.error && !props.disabled && !props.readOnly) {
            style.iconBackground = listOf(ODSColorModel(hexColor = scheme.basicAccent))
        }
        if (props.selected == ODSCheckboxIconSelected.INDETERMINATE && props.error && !props.disabled && !props.readOnly) {
            style.iconBackground =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
        }
        if (props.selected == ODSCheckboxIconSelected.SELECTED && props.error && !props.disabled && !props.readOnly) {
            style.iconBackground =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
        }
        if (props.selected == ODSCheckboxIconSelected.UNSELECTED && !props.error && !props.disabled && props.readOnly) {
            style.iconBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.iconBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.selected == ODSCheckboxIconSelected.UNSELECTED && !props.disabled && !props.readOnly && state == ODSActions.PRESSED) {
            style.iconBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundPressed))
        }
        if (props.selected == ODSCheckboxIconSelected.UNSELECTED && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.iconBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        if (props.selected == ODSCheckboxIconSelected.UNSELECTED && props.error && !props.disabled && !props.readOnly) {
            style.iconBorder = DSCheckboxIconTokens.iconBorderSelectedUnselectedError
            style.iconBorderColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
        }
        if (props.selected == ODSCheckboxIconSelected.UNSELECTED && !props.error && !props.disabled && !props.readOnly) {
            style.iconBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.selected == ODSCheckboxIconSelected.INDETERMINATE && !props.error && !props.disabled && !props.readOnly && state == ODSActions.PRESSED) {
            style.iconBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedAccentPressed))
        }
        if (props.selected == ODSCheckboxIconSelected.SELECTED && !props.error && !props.disabled && !props.readOnly && state == ODSActions.PRESSED) {
            style.iconBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedAccentPressed))
        }
        if (props.selected == ODSCheckboxIconSelected.INDETERMINATE && props.error && !props.disabled && !props.readOnly && state == ODSActions.PRESSED) {
            style.iconBackground =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructivePressed))
        }
        if (props.selected == ODSCheckboxIconSelected.SELECTED && props.error && !props.disabled && !props.readOnly && state == ODSActions.PRESSED) {
            style.iconBackground =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructivePressed))
        }
        if (props.selected == ODSCheckboxIconSelected.INDETERMINATE && !props.error && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.iconBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverAccentHover))
        }
        if (props.selected == ODSCheckboxIconSelected.SELECTED && !props.error && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.iconBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverAccentHover))
        }
        if (props.selected == ODSCheckboxIconSelected.INDETERMINATE && props.error && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.iconBackground =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveHovered))
        }
        if (props.selected == ODSCheckboxIconSelected.SELECTED && props.error && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.iconBackground =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveHovered))
        }
        if (props.selected == ODSCheckboxIconSelected.UNSELECTED && !props.error && !props.disabled && !props.readOnly && state == ODSActions.PRESSED) {
            style.iconBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedStrokePressed))
        }
        if (props.selected == ODSCheckboxIconSelected.UNSELECTED && props.error && !props.disabled && !props.readOnly && state == ODSActions.PRESSED) {
            style.iconBorderColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructivePressed))
        }
        if (props.selected == ODSCheckboxIconSelected.UNSELECTED && !props.error && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.iconBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverStrokeHover))
        }
        if (props.selected == ODSCheckboxIconSelected.UNSELECTED && props.error && !props.disabled && !props.readOnly && state == ODSActions.HOVERED) {
            style.iconBorderColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveHovered))
        }
        style.minusWidth = DSCheckboxIconTokens.minusWidth
        style.minusHeight = DSCheckboxIconTokens.minusHeight
        if (props.selected == ODSCheckboxIconSelected.UNSELECTED) {
            style.minusColor = scheme.basicTextOnAccent
        }
        if (props.selected == ODSCheckboxIconSelected.INDETERMINATE && props.error) {
            style.minusColor = scheme.basicTextOnAccent
        }
        if (props.selected == ODSCheckboxIconSelected.SELECTED && props.error) {
            style.minusColor = scheme.basicTextOnAccent
        }
        if (props.selected == ODSCheckboxIconSelected.SELECTED && !props.error && props.disabled && !props.readOnly) {
            style.minusColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.selected == ODSCheckboxIconSelected.INDETERMINATE && !props.error && !props.disabled && props.readOnly) {
            style.minusColor = scheme.basicText
        }
        if (props.selected == ODSCheckboxIconSelected.SELECTED && !props.error && !props.disabled && props.readOnly) {
            style.minusColor = scheme.basicText
        }
        if (props.selected == ODSCheckboxIconSelected.INDETERMINATE && !props.error && props.disabled && !props.readOnly) {
            style.minusColor = scheme.interactionStatesDisabledTextOnAccentDisabled
        }
        if (props.selected == ODSCheckboxIconSelected.INDETERMINATE && !props.error && !props.disabled && !props.readOnly) {
            style.minusColor = scheme.basicTextOnAccent
        }
        if (props.selected == ODSCheckboxIconSelected.SELECTED && !props.error && !props.disabled && !props.readOnly) {
            style.minusColor = scheme.basicTextOnAccent
        }
        style.checkmarkWidth = DSCheckboxIconTokens.checkmarkWidth
        style.checkmarkHeight = DSCheckboxIconTokens.checkmarkHeight
        if (props.selected == ODSCheckboxIconSelected.UNSELECTED) {
            style.checkmarkColor = scheme.basicTextOnAccent
        }
        if (props.selected == ODSCheckboxIconSelected.INDETERMINATE && props.error) {
            style.checkmarkColor = scheme.basicTextOnAccent
        }
        if (props.selected == ODSCheckboxIconSelected.SELECTED && props.error) {
            style.checkmarkColor = scheme.basicTextOnAccent
        }
        if (props.selected == ODSCheckboxIconSelected.SELECTED && !props.error && props.disabled && !props.readOnly) {
            style.checkmarkColor = scheme.interactionStatesDisabledTextOnAccentDisabled
        }
        if (props.selected == ODSCheckboxIconSelected.INDETERMINATE && !props.error && !props.disabled && props.readOnly) {
            style.checkmarkColor = scheme.basicText
        }
        if (props.selected == ODSCheckboxIconSelected.SELECTED && !props.error && !props.disabled && props.readOnly) {
            style.checkmarkColor = scheme.basicText
        }
        if (props.selected == ODSCheckboxIconSelected.INDETERMINATE && !props.error && props.disabled && !props.readOnly) {
            style.checkmarkColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.selected == ODSCheckboxIconSelected.INDETERMINATE && !props.error && !props.disabled && !props.readOnly) {
            style.checkmarkColor = scheme.basicTextOnAccent
        }
        if (props.selected == ODSCheckboxIconSelected.SELECTED && !props.error && !props.disabled && !props.readOnly) {
            style.checkmarkColor = scheme.basicTextOnAccent
        }
        return style
    }
}
