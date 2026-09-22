package com.telekom.odsystem.atoms.radioicon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSRadioIconTokens
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod", "ComplexCondition")
class ODSRadioIconStyle {
    var padding: ODSPadding? = null
    var width: Dp? = null
    var height: Dp? = null
    var clipContent: Boolean? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var iconBackgroundColor: List<ODSColorModel>? = null
    var iconBorderRadius: ODSCorners? = null
    var iconBorder: Dp? = null
    var iconBorderColor: List<ODSColorModel>? = null
    var iconVerticalAlignment: Alignment.Vertical? = null
    var iconHorizontalAlignment: Alignment.Horizontal? = null
    var iconHorizontalArrangement: Arrangement.Horizontal? = null
    var innerCircleBackgroundColor: List<ODSColorModel>? = null
    var innerCircleBorderRadius: ODSCorners? = null
    var innerCircleWidth: Dp? = null
    var innerCircleHeight: Dp? = null
    var innerCircleClipContent: Boolean? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSRadioIconProps,
        state: ODSActions
    ): ODSRadioIconStyle {
        val style = ODSRadioIconStyle()
        style.padding = DSRadioIconTokens.padding
        style.clipContent = DSRadioIconTokens.clipContent
        style.verticalAlignment = DSRadioIconTokens.verticalAlignment
        style.horizontalAlignment = DSRadioIconTokens.horizontalAlignment
        style.horizontalArrangement = DSRadioIconTokens.horizontalArrangement
        if (state == ODSActions.PRESSED) {
            style.padding = DSRadioIconTokens.paddingStatePressed
        }
        if (props.size == ODSRadioIconSize.SMALL) {
            style.width = DSRadioIconTokens.widthSizeSmall
            style.height = DSRadioIconTokens.heightSizeSmall
        }
        if (props.size == ODSRadioIconSize.LARGE) {
            style.width = DSRadioIconTokens.widthSizeLarge
            style.height = DSRadioIconTokens.heightSizeLarge
        }
        if (!props.disabled && !props.readonly && state == ODSActions.HOVERED) {
            style.padding = DSRadioIconTokens.paddingStateHovered
        }
        style.iconBorderRadius = DSRadioIconTokens.iconBorderRadius
        style.iconVerticalAlignment = DSRadioIconTokens.iconVerticalAlignment
        style.iconHorizontalAlignment = DSRadioIconTokens.iconHorizontalAlignment
        style.iconHorizontalArrangement = DSRadioIconTokens.iconHorizontalArrangement
        if (!props.selected && !props.error) {
            style.iconBorder = DSRadioIconTokens.iconBorder
        }
        if (props.selected && !props.error && props.disabled) {
            style.iconBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledAccentDisabled))
        }
        if (!props.selected && !props.error && props.disabled) {
            style.iconBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.iconBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (!props.selected && !props.disabled && !props.readonly) {
            style.iconBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        }
        if (props.selected && !props.error && !props.disabled && props.readonly) {
            style.iconBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledAccentDisabled))
        }
        if (props.selected && !props.error && !props.disabled && !props.readonly) {
            style.iconBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicAccent))
        }
        if (props.selected && props.error && !props.disabled && !props.readonly) {
            style.iconBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
        }
        if (!props.selected && !props.error && !props.disabled && props.readonly) {
            style.iconBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.iconBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (!props.selected && !props.disabled && !props.readonly && state == ODSActions.PRESSED) {
            style.iconBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundPressed))
        }
        if (!props.selected && !props.disabled && !props.readonly && state == ODSActions.HOVERED) {
            style.iconBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
        }
        if (!props.selected && props.error && !props.disabled && !props.readonly) {
            style.iconBorder = DSRadioIconTokens.iconBorderError
            style.iconBorderColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
        }
        if (!props.selected && !props.error && !props.disabled && !props.readonly) {
            style.iconBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (props.selected && !props.error && !props.disabled && !props.readonly && state == ODSActions.PRESSED) {
            style.iconBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedAccentPressed))
        }
        if (props.selected && props.error && !props.disabled && !props.readonly && state == ODSActions.PRESSED) {
            style.iconBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructivePressed))
        }
        if (props.selected && !props.error && !props.disabled && !props.readonly && state == ODSActions.HOVERED) {
            style.iconBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverAccentHover))
        }
        if (props.selected && props.error && !props.disabled && !props.readonly && state == ODSActions.HOVERED) {
            style.iconBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveHovered))
        }
        if (!props.selected && !props.error && !props.disabled && !props.readonly && state == ODSActions.PRESSED) {
            style.iconBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedStrokePressed))
        }
        if (!props.selected && props.error && !props.disabled && !props.readonly && state == ODSActions.PRESSED) {
            style.iconBorderColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructivePressed))
        }
        if (!props.selected && !props.error && !props.disabled && !props.readonly && state == ODSActions.HOVERED) {
            style.iconBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverStrokeHover))
        }
        if (!props.selected && props.error && !props.disabled && !props.readonly && state == ODSActions.HOVERED) {
            style.iconBorderColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveHovered))
        }
        style.innerCircleBorderRadius = DSRadioIconTokens.innerCircleBorderRadius
        style.innerCircleClipContent = DSRadioIconTokens.innerCircleClipContent
        if (!props.selected && !props.error && props.disabled) {
            style.innerCircleBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
        }
        if (!props.selected && !props.disabled && !props.readonly) {
            style.innerCircleBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.basicTextOnAccent))
        }
        if (props.selected && !props.disabled && !props.readonly) {
            style.innerCircleBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.basicTextOnAccent))
        }
        if (props.selected && !props.error && props.disabled && !props.readonly) {
            style.innerCircleBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledTextOnAccentDisabled))
        }
        if (!props.selected && !props.error && !props.disabled && props.readonly) {
            style.innerCircleBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
        }
        if (props.selected && !props.error && !props.disabled && props.readonly) {
            style.innerCircleBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
        }
        if (props.selected && !props.disabled && !props.readonly && state == ODSActions.PRESSED) {
            style.innerCircleBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedTextOnAccentPressed))
        }
        if (props.selected && !props.disabled && !props.readonly && state == ODSActions.HOVERED) {
            style.innerCircleBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverTextOnAccentHover))
        }
        if (props.size == ODSRadioIconSize.LARGE && !props.disabled && !props.readonly && state == ODSActions.HOVERED) {
            style.innerCircleWidth = DSRadioIconTokens.innerCircleWidthSizeLargeStateHovered
            style.innerCircleHeight = DSRadioIconTokens.innerCircleHeightSizeLargeStateHovered
        }
        if (props.size == ODSRadioIconSize.SMALL && !props.disabled && !props.readonly && state == ODSActions.HOVERED) {
            style.innerCircleWidth = DSRadioIconTokens.innerCircleWidthSizeSmallStateHovered
            style.innerCircleHeight = DSRadioIconTokens.innerCircleHeightSizeSmallStateHovered
        }
        // Custom addition
        if (props.size == ODSRadioIconSize.SMALL) {
            style.innerCircleWidth = DSRadioIconTokens.innerCircleWidthSizeSmall
            style.innerCircleHeight = DSRadioIconTokens.innerCircleHeightSizeSmall
        }
        if (props.size == ODSRadioIconSize.LARGE) {
            style.innerCircleWidth = DSRadioIconTokens.innerCircleWidthSizeLarge
            style.innerCircleHeight = DSRadioIconTokens.innerCircleHeightSizeLarge
        }
        if (state == ODSActions.PRESSED) {
            if (props.size == ODSRadioIconSize.SMALL) {
                style.innerCircleWidth = DSRadioIconTokens.innerCircleWidthStatePressedSizeSmall
                style.innerCircleHeight = DSRadioIconTokens.innerCircleHeightStatePressedSizeSmall
            }
            if (props.size == ODSRadioIconSize.LARGE) {
                style.innerCircleWidth = DSRadioIconTokens.innerCircleWidthStatePressedSizeLarge
                style.innerCircleHeight = DSRadioIconTokens.innerCircleHeightStatePressedSizeLarge
            }
        }
        return style
    }
}
