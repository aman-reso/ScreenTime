package com.telekom.odsystem.atoms.switchicon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSSwitchIconTokens
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSSwitchIconStyle {
    var width: Dp? = null
    var height: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null // Not used in mobile
    var verticalArrangement: Arrangement.Vertical? = null
    var contentAlignment: Alignment? = null
    var strokeBackgroundColor: List<ODSColorModel>? = null
    var strokeBorderRadius: ODSCorners? = null
    var strokeBorder: Dp? = null
    var strokeBorderColor: List<ODSColorModel>? = null
    var strokeHeight: Dp? = null
    var strokeClipContent: Boolean? = null
    var strokeContentAlignment: Alignment? = null
    var handleContainerPadding: ODSPadding? = null
    var handleContainerVerticalAlignment: Alignment.Vertical? = null
    var handleContainerHorizontalAlignment: Alignment.Horizontal? = null
    var handleContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var handleBackgroundColor: List<ODSColorModel>? = null
    var handleBorderRadius: ODSCorners? = null
    var handleWidth: Dp? = null
    var handleHeight: Dp? = null
    var handleClipContent: Boolean? = null
    var scaleFactor: Float? = null // Not exported by plugin.
    fun getStyle(
        scheme: ODSTheme,
        props: ODSSwitchIconProps,
        state: ODSActions
    ): ODSSwitchIconStyle {
        val style = ODSSwitchIconStyle()
        style.verticalAlignment = DSSwitchIconTokens.verticalAlignment
        style.verticalArrangement = DSSwitchIconTokens.verticalArrangement
        if (props.selected) {
            style.horizontalAlignment = DSSwitchIconTokens.horizontalAlignmentSelected
            style.contentAlignment = DSSwitchIconTokens.contentAlignmentSelected
        }
        if (!props.selected) {
            style.horizontalAlignment = DSSwitchIconTokens.horizontalAlignment
            style.contentAlignment = DSSwitchIconTokens.contentAlignment
        }
        if (props.size == ODSSwitchIconSize.LARGE) {
            style.width = DSSwitchIconTokens.widthSizeLarge
            style.height = DSSwitchIconTokens.heightSizeLarge
        }
        if (props.size == ODSSwitchIconSize.SMALL) {
            style.width = DSSwitchIconTokens.widthSizeSmall
            style.height = DSSwitchIconTokens.heightSizeSmall
        }
        style.strokeBorderRadius = DSSwitchIconTokens.strokeBorderRadius
        style.strokeClipContent = DSSwitchIconTokens.strokeClipContent
        style.strokeContentAlignment = DSSwitchIconTokens.strokeContentAlignment
        if (!props.selected) {
            style.strokeBorder = DSSwitchIconTokens.strokeBorder
        }
        if (props.size == ODSSwitchIconSize.LARGE) {
            style.strokeHeight = DSSwitchIconTokens.strokeHeightSizeLarge
        }
        if (props.size == ODSSwitchIconSize.SMALL) {
            style.strokeHeight = DSSwitchIconTokens.strokeHeightSizeSmall
        }
//        if (props.size == ODSSwitchIconSize.LARGE && state == ODSActions.PRESSED) {
//            style.strokeHeight = DSSwitchIconTokens.strokeHeightSizeLargeStatePressed
//        }
//        if (props.size == ODSSwitchIconSize.SMALL && state == ODSActions.PRESSED) {
//            style.strokeHeight = DSSwitchIconTokens.strokeHeightSizeSmallStatePressed
//        }
        if (!props.selected && !props.readOnly && !props.disabled) {
            style.strokeBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackground))
            style.strokeBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (!props.selected && props.readOnly && !props.disabled) {
            style.strokeBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.strokeBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (!props.selected && !props.readOnly && props.disabled) {
            style.strokeBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
            style.strokeBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeDisabled))
        }
        if (props.selected && !props.readOnly && !props.disabled) {
            style.strokeBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicAccent))
        }
        if (props.selected && props.readOnly && !props.disabled) {
            style.strokeBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledAccentDisabled))
        }
        if (props.selected && !props.readOnly && props.disabled) {
            style.strokeBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledAccentDisabled))
        }
        if (!props.selected && !props.readOnly && !props.disabled && state == ODSActions.HOVERED) {
            style.strokeBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundHover))
            style.strokeBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverStrokeHover))
        }
        if (!props.selected && !props.readOnly && !props.disabled && state == ODSActions.PRESSED) {
            style.strokeBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundPressed))
            style.strokeBorderColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedStrokePressed))
        }
        if (props.selected && !props.readOnly && !props.disabled && state == ODSActions.HOVERED) {
            style.strokeBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverAccentHover))
        }
        if (props.selected && !props.readOnly && !props.disabled && state == ODSActions.PRESSED) {
            style.strokeBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedAccentPressed))
        }
//        if (props.size == ODSSwitchIconSize.LARGE && !props.readOnly && !props.disabled && state == ODSActions.HOVERED) {
//            style.strokeHeight = DSSwitchIconTokens.strokeHeightSizeLargeStateHovered
//        }
//        if (props.size == ODSSwitchIconSize.SMALL && !props.readOnly && !props.disabled && state == ODSActions.HOVERED) {
//            style.strokeHeight = DSSwitchIconTokens.strokeHeightSizeSmallStateHovered
//        }
        style.handleContainerPadding = DSSwitchIconTokens.handleContainerPadding
        style.handleContainerVerticalAlignment = DSSwitchIconTokens.handleContainerVerticalAlignment
        style.handleContainerHorizontalAlignment =
            DSSwitchIconTokens.handleContainerHorizontalAlignment
        style.handleContainerHorizontalArrangement =
            DSSwitchIconTokens.handleContainerHorizontalArrangement
        style.handleBorderRadius = DSSwitchIconTokens.handleBorderRadius
        style.handleClipContent = DSSwitchIconTokens.handleClipContent
        if (props.size == ODSSwitchIconSize.LARGE) {
            style.handleWidth = DSSwitchIconTokens.handleWidthSizeLarge
            style.handleHeight = DSSwitchIconTokens.handleHeightSizeLarge
        }
        if (props.size == ODSSwitchIconSize.SMALL) {
            style.handleWidth = DSSwitchIconTokens.handleWidthSizeSmall
            style.handleHeight = DSSwitchIconTokens.handleHeightSizeSmall
        }
        if (!props.selected && !props.disabled) {
            style.handleBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
        }
        if (props.selected && props.readOnly && !props.disabled) {
            style.handleBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
        }
        if (!props.selected && !props.readOnly && props.disabled) {
            style.handleBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledTextDisabled))
        }
        if (props.selected && !props.readOnly && !props.disabled) {
            style.handleBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicTextOnAccent))
        }
        if (props.selected && !props.readOnly && props.disabled) {
            style.handleBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledTextOnAccentDisabled))
        }
        if (!props.selected && !props.readOnly && !props.disabled && state == ODSActions.HOVERED) {
            style.handleBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverTextHover))
        }
        if (!props.selected && !props.readOnly && !props.disabled && state == ODSActions.PRESSED) {
            style.handleBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedTextPressed))
        }

        // Custom Addition
        style.scaleFactor = DSSwitchIconTokens.scaleFactor // Not exported by plugin
        return style
    }
}
